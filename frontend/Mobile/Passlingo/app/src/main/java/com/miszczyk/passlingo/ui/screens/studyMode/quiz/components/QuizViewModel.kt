package com.miszczyk.passlingo.ui.screens.studyMode.quiz.components

import android.app.Application
import android.util.Log
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
import com.miszczyk.passlingo.ui.screens.studyMode.quiz.model.QuizUiState
import com.miszczyk.passlingo.ui.screens.studyMode.quiz.model.TypeAnswer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionRepository = StudySessionRepositoryImpl(context = application)
    private val timeRepository = TimeAndAppsRepository(context = application)
    private val deckRepository = DeckRepository(context = application)
    private val _uiState = MutableStateFlow(value = QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()


    private var currentDeckId: String = ""
    private var targetRounds: Int = 1
    private var currentSessionId: String = ""
    private var currentBatch: List<StudyCardProgressEntity> = emptyList()
    private var typingDict: Map<String, Pair<String, String>> = emptyMap()
    private var timeToBreath = 0
    private var totalCardsInSession: Int = 0
    private var allCards: List<StudyCardProgressEntity> = emptyList()

    fun startSession(deckId: String, rounds: Int){
        currentDeckId = deckId
        timeToBreath = 0

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isBreather = false,
                    userAnswer = TypeAnswer.NONE
                )
            }

            runCatching {
                val deckWithCards = deckRepository.getDeckWithFlashcardsById(deckId)

                typingDict = deckWithCards?.flashcards?.associate {
                    it.id to Pair(first = it.front, second = it.back)
                } ?: emptyMap()

                val activeSession = sessionRepository.getActiveSession(deckId, StudyMode.QUIZ)

                if(activeSession != null){
                    currentSessionId = activeSession.session.id
                    targetRounds = activeSession.session.targetRounds
                } else {
                    targetRounds = rounds
                    createNewSession(deckId, deckWithCards)
                }

                allCards = sessionRepository.getAllCards(currentSessionId)
                totalCardsInSession = sessionRepository.getTotalCardCount(currentSessionId)
                loadNextBatchAndShow()
            }.onFailure { error ->
                Log.e("QuizViewModel", "Failed to start session", error)
                _uiState.update {
                    it.copy(errorMessage = getApplication<Application>().getString(R.string.error_study_session))
                }
            }
        }
    }

    private suspend fun createNewSession(deckId: String, deckWithCard: DeckWithFlashcards?){
        currentSessionId = UUID.randomUUID().toString()
        val session = StudySessionEntity(
            id = currentSessionId,
            deckId = deckId,
            mode = StudyMode.QUIZ,
            targetRounds = targetRounds
        )

        val progressList = deckWithCard?.flashcards?.shuffled()?.mapIndexed { index, card ->
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
                Log.e("QuizViewModel", "Failed to continue learning", error)
                _uiState.update {
                    it.copy(errorMessage = getApplication<Application>().getString(R.string.error_study_session))
                }
            }
        }
    }

    fun checkUserAnswer(selectedText: String){
        val isCorrect = selectedText == _uiState.value.currentBack

        _uiState.update {
            it.copy(
                selectedAnswer = selectedText,
                userAnswer = if (isCorrect) TypeAnswer.GOOD else TypeAnswer.BAD
            )
        }
    }

    private suspend fun showCurrentCard(){
        val currentProgress = currentBatch.firstOrNull() ?: return

        val flashcardData = typingDict[currentProgress.flashcardId]
        val correctAnswer = flashcardData?.second ?: "Unknown"
        val finished = sessionRepository.getFinishedCard(currentSessionId, targetRounds)

        val options = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
            val allMeanings =
                allCards.mapNotNull { progress ->
                    typingDict[progress.flashcardId]?.second
                }.distinct()
            val wrongAnswers = allMeanings
                .filter { !it.equals(correctAnswer, ignoreCase = true) }
                .sortedBy { levenshteinDistance(it, correctAnswer) }
                .take(3)
            (wrongAnswers + correctAnswer).shuffled()
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                currentFront = flashcardData?.first ?: "Unknown",
                currentBack = correctAnswer,
                options = options,
                selectedAnswer = null,
                userAnswer = TypeAnswer.NONE,
                progressText = "$finished / $totalCardsInSession"
            )
        }
    }

    private fun levenshteinDistance(correctAnswer: String, proposition: String): Int {
        val m = correctAnswer.length
        val n = proposition.length
        var cost = IntArray(m + 1) { it }
        var newCost = IntArray(m + 1) { 0 }

        for(i in 1..n){
            newCost[0] = i
            for(j in 1..m){
                val match = if (correctAnswer[j - 1] == proposition[i - 1]) 0 else 1
                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1
                newCost[j] = minOf(costInsert, costDelete, costReplace)
            }
            val swap = cost
            cost = newCost
            newCost = swap
        }
        return cost[m]
    }

    private suspend fun loadNextBatchAndShow(){
        currentBatch = sessionRepository.getNextBatch(currentSessionId, targetRounds)
        if(currentBatch.isEmpty()){
            loadCardsToPractice()

            if(currentSessionId.isNotEmpty()){
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
            val currentProgress = currentBatch.firstOrNull() ?: return@launch
            val isGoodAnswer = _uiState.value.userAnswer == TypeAnswer.GOOD

            runCatching {
                if(isGoodAnswer){
                    sessionRepository.incrementCurrentRound(currentSessionId, currentProgress.flashcardId)
                    if (currentProgress.currentRound + 1 == targetRounds) {
                        timeRepository.addCreditTime(secondsEarned = 10L * targetRounds)
                    }
                } else {
                    sessionRepository.resetCurrentRound(currentSessionId, currentProgress.flashcardId)
                    sessionRepository.incrementAttempts(currentSessionId, currentProgress.flashcardId)
                }

                timeToBreath++
                currentBatch = currentBatch.drop(1)

                when {
                    currentBatch.isNotEmpty() -> showCurrentCard()
                    timeToBreath >= 10 -> _uiState.update { it.copy(isBreather = true) }
                    else -> loadNextBatchAndShow()
                }
            }.onFailure {
                Log.e("QuizViewModel", "Failed to update card progress", it)
                _uiState.update { state ->
                    state.copy(errorMessage = getApplication<Application>().getString(R.string.error_study_session))
                }
            }
        }
    }
}