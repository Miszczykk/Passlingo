package com.miszczyk.passlingo.ui.screens.studyMode.typing.components

import android.app.Application
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.data.local.entity.DeckWithFlashcards
import com.miszczyk.passlingo.data.local.entity.StudyCardProgressEntity
import com.miszczyk.passlingo.data.local.entity.StudyMode
import com.miszczyk.passlingo.data.local.entity.StudySessionEntity
import com.miszczyk.passlingo.data.repository.DeckRepository
import com.miszczyk.passlingo.data.repository.StudySessionRepositoryImpl
import com.miszczyk.passlingo.ui.screens.home.data.TimeAndAppsRepository
import com.miszczyk.passlingo.ui.screens.studyMode.model.PracticeCardUiModel
import com.miszczyk.passlingo.ui.screens.studyMode.typing.model.TypeAnswer
import com.miszczyk.passlingo.ui.screens.studyMode.typing.model.TypingUiState
import com.miszczyk.passlingo.ui.util.clear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class TypingViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionRepository = StudySessionRepositoryImpl(context = application)
    private val timeRepository = TimeAndAppsRepository(context = application)
    private val deckRepository = DeckRepository(context = application)

    private val _uiState = MutableStateFlow(value = TypingUiState())
    val uiState: StateFlow<TypingUiState> = _uiState.asStateFlow()

    private var currentDeckId: String = ""
    private var targetRounds: Int = 1
    private var currentSessionId: String = ""
    private var currentBatch: List<StudyCardProgressEntity> = emptyList()
    private var typingDict: Map<String, Pair<String, String>> = emptyMap()
    private var timeToBreath = 0
    private var repeatCard: StudyCardProgressEntity? = null

    fun startSession(deckId: String, rounds: Int) {
        currentDeckId = deckId
        timeToBreath = 0

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isBreather = false,
                    userAnswer = TypeAnswer.NONE,
                    userAnswerState = TextFieldState(initialText = "")
                )
            }

            runCatching {
                val deckWithCards = deckRepository.getDeckWithFlashcardsById(deckId)

                typingDict = deckWithCards?.flashcards?.associate {
                    it.id to Pair(first = it.front, second = it.back)
                } ?: emptyMap()

                val activeSession = sessionRepository.getActiveSession(deckId, StudyMode.TYPING)

                if (activeSession != null) {
                    currentSessionId = activeSession.session.id
                    targetRounds = activeSession.session.targetRounds
                } else {
                    targetRounds = rounds
                    createNewSession(deckId, deckWithCards)
                }
                loadNextBatchAndShow()
            }.onFailure { error ->
                Log.e("TypingViewModel", "Failed to start session", error)
                _uiState.update {
                    it.copy(errorMessage = getApplication<Application>().getString(R.string.error_study_session))
                }
            }
        }
    }

    private suspend fun createNewSession(deckId: String, deckWithCards: DeckWithFlashcards?) {
        currentSessionId = UUID.randomUUID().toString()
        val session = StudySessionEntity(
            id = currentSessionId,
            deckId = deckId,
            mode = StudyMode.TYPING,
            targetRounds = targetRounds
        )

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


    fun continueLearningClicked() {
        viewModelScope.launch {
            runCatching {
                timeToBreath = 0
                _uiState.update { it.copy(isBreather = false) }
                loadNextBatchAndShow()
            }.onFailure { error ->
                Log.e("TypingViewModel", "Failed to continue learning", error)
                _uiState.update {
                    it.copy(errorMessage = getApplication<Application>().getString(R.string.error_study_session))
                }
            }
        }
    }


    fun checkUserAnswer(userAnswer: TextFieldState, correctAnswer: String?) {
        if (userAnswer.text.toString() == correctAnswer) {
            _uiState.update { it.copy(userAnswer = TypeAnswer.GOOD) }
            timeToBreath++
        } else {
            _uiState.update { it.copy(userAnswer = TypeAnswer.BAD) }
            if (repeatCard == null) {
                repeatCard = currentBatch.firstOrNull()
            }
        }
    }

    private suspend fun showCurrentCard() {
        val currentProgress = repeatCard ?: currentBatch.firstOrNull() ?: return

        val flashcardData = typingDict[currentProgress.flashcardId]
        val finished = sessionRepository.getFinishedCard(currentSessionId, targetRounds)
        val total = sessionRepository.getTotalCardCount(currentSessionId)

        _uiState.update {
            it.copy(
                isLoading = false,
                currentFront = flashcardData?.first ?: "Unknown",
                currentBack = flashcardData?.second ?: "Unknown",
                progressText = "$finished / $total"
            )
        }
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
            val texts = typingDict[progress.flashcardId]
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

    fun moveToNextCard(){
        viewModelScope.launch {
            val currentProgress = repeatCard ?: currentBatch.firstOrNull() ?: return@launch
            val isGoodAnswer = _uiState.value.userAnswer == TypeAnswer.GOOD

            runCatching {
                if(isGoodAnswer){
                    sessionRepository.incrementCurrentRound(currentSessionId, currentProgress.flashcardId)
                    if (repeatCard != null) {
                        repeatCard = null
                    } else {
                        if(currentProgress.currentRound + 1 == targetRounds){
                            timeRepository.addCreditTime(secondsEarned = ((10 * targetRounds).toLong()))
                        }
                        currentBatch = currentBatch.drop(1)
                    }

                }else{
                    sessionRepository.resetCurrentRound(currentSessionId, currentProgress.flashcardId)
                    sessionRepository.incrementAttempts(currentSessionId, currentProgress.flashcardId)

                    if (repeatCard != null && currentBatch.firstOrNull()?.id == repeatCard?.id) {
                        currentBatch = currentBatch.drop(1)
                    }
                }

                _uiState.update { it.copy(userAnswer = TypeAnswer.NONE) }

                if(currentBatch.isNotEmpty() || repeatCard != null){
                    showCurrentCard()
                }else{
                    if(timeToBreath >= 10){
                        _uiState.update { it.copy(isBreather = true) }
                    }else{
                        loadNextBatchAndShow()
                    }
                }

            }.onFailure { it ->
                Log.e("TypingViewModel", "Failed to update card progress", it)
                _uiState.update {
                    it.copy(errorMessage = getApplication<Application>().getString(R.string.error_study_session))
                }
            }
            _uiState.value.userAnswerState.clear()
        }
    }
    fun checkAgain(userAnswer: TextFieldState, correctAnswer: String?) {
    //TODO implement AI
}
}


