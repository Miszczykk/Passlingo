package com.miszczyk.passlingo.ui.screens.studyMode.flashcards.components

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.data.local.entity.StudyCardProgressEntity
import com.miszczyk.passlingo.data.local.entity.StudyMode
import com.miszczyk.passlingo.data.local.entity.StudySessionEntity
import com.miszczyk.passlingo.data.repository.DeckRepository
import com.miszczyk.passlingo.data.repository.StudySessionRepositoryImpl
import com.miszczyk.passlingo.ui.screens.home.data.TimeAndAppsRepository
import com.miszczyk.passlingo.ui.screens.studyMode.flashcards.model.FlashcardsUiState
import com.miszczyk.passlingo.ui.screens.studyMode.flashcards.model.PracticeCardUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class FlashcardsViewModel(application: Application) : AndroidViewModel(application){
    private val sessionRepository = StudySessionRepositoryImpl(application)
    private val repository = TimeAndAppsRepository(context = application)
    private val deckRepository = DeckRepository(application)

    private val _uiState = MutableStateFlow(FlashcardsUiState())
    val uiState: StateFlow<FlashcardsUiState> = _uiState.asStateFlow()

    private var currentDeckId: String = ""
    private var targetRounds: Int = 1
    private var currentSessionId: String = ""

    private var currentBatch: List<StudyCardProgressEntity> = emptyList()
    private var flashcardsDict: Map<String, Pair<String, String>> = emptyMap()
    var timeToBreath = 0

    fun startSession(deckId: String, rounds: Int){
        currentDeckId = deckId
        timeToBreath = 0
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = false, isBreather = false, isFlipped = false) }

            val deckWithCards = deckRepository.getDeckWithFlashcardsById(deckId)
            flashcardsDict = deckWithCards?.flashcards?.associate {
                it.id to Pair(it.front, it.back)
            } ?: emptyMap()

            if (sessionRepository.doesSessionExist(deckId)) {
                val session = sessionRepository.getActiveSession(deckId)
                if (session != null) {
                    currentSessionId = session.session.id
                    targetRounds = session.session.targetRounds
                }
            } else {
                targetRounds = rounds
                createNewSession(deckId)
            }
            loadNextBatchAndShow()
        }
    }

    private suspend fun createNewSession(deckId: String){
        currentSessionId = UUID.randomUUID().toString()
        val session = StudySessionEntity(
            id = currentSessionId,
            deckId = deckId,
            mode = StudyMode.FLASHCARDS,
            targetRounds = targetRounds
        )

        val deckWithCards = deckRepository.getDeckWithFlashcardsById(deckId)
        val progressList = deckWithCards?.flashcards?.shuffled()?.mapIndexed { index, card ->
            StudyCardProgressEntity(
                id = UUID.randomUUID().toString(),
                sessionId = currentSessionId,
                flashcardId = card.id,
                currentRound = 0,
                attempts = 0,
                orderIndex = index
            )
        } ?: emptyList()

        sessionRepository.createSession(session, progressList)
    }

    private suspend fun loadNextBatchAndShow(){
        currentBatch = sessionRepository.getNextBatch(currentSessionId, targetRounds)
        if(currentBatch.isEmpty()){
            loadCardsToPractice()

            if(currentSessionId.isNotEmpty()) {
                sessionRepository.deleteSessionById(currentSessionId)
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    currentFront = null,
                    currentBack = null
                )
            }

        }else{
            showCurrentCard()
        }
    }

    suspend fun loadCardsToPractice(){
        val progressCards = sessionRepository.getCardToPractice(currentSessionId)
        val mappedCards = progressCards.map { progress ->
            val texts = flashcardsDict[progress.flashcardId]
            PracticeCardUiModel(
                id = progress.id,
                front = texts?.first ?: "Unknown",
                back = texts?.second ?: "Unknown",
                attempts = progress.attempts
            )
        }
        _uiState.update {
            it.copy(cardsToPractice = mappedCards)
        }
    }


    private suspend fun showCurrentCard(){
        if(currentBatch.isEmpty()) return

        val currentProgress = currentBatch.first()
        val flashcardData = flashcardsDict[currentProgress.flashcardId]

        val finished = sessionRepository.getFinishedCard(currentSessionId, targetRounds)
        val total = sessionRepository.getTotalCardCount(currentSessionId)

        _uiState.update {
            it.copy(
                isLoading = false,
                isFlipped = false,
                currentFront = flashcardData?.first ?: "Unknown",
                currentBack = flashcardData?.second ?: "Unknown",
                progressText = "$finished / $total"
            )
        }
    }

    fun flipCard() {
        _uiState.update { it.copy(isFlipped = !it.isFlipped) }
    }

    fun answerCard(isCorrect: Boolean){
        if(currentBatch.isEmpty()) return

        val currentProgress = currentBatch.first()
        timeToBreath++

        viewModelScope.launch {
            if(isCorrect){
                sessionRepository.incrementCurrentRound(currentSessionId, currentProgress.flashcardId)
                if(currentProgress.currentRound + 1 == targetRounds){
                    repository.addCreditTime(secondsEarned = ((10 * targetRounds).toLong()))
                }
            }else{
                sessionRepository.resetCurrentRound(currentSessionId, currentProgress.flashcardId)
                sessionRepository.incrementAttempts(currentSessionId, currentProgress.flashcardId)
            }

            currentBatch = currentBatch.drop(1)

            if(currentBatch.isNotEmpty()){
                showCurrentCard()
            }else{
                if(timeToBreath >= 10){
                    _uiState.update { it.copy(isBreather = true) }
                }else{
                    loadNextBatchAndShow()
                }

            }
        }
    }

    fun continueLearningClicked(){
        viewModelScope.launch {
            timeToBreath = 0
            _uiState.update { it.copy(isBreather = false) }
            loadNextBatchAndShow()
        }
    }
}