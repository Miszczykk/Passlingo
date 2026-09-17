package com.miszczyk.passlingo.ui.screens.studyMode.quiz.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.data.local.entity.StudyCardProgressEntity
import com.miszczyk.passlingo.data.local.entity.StudyMode
import com.miszczyk.passlingo.ui.screens.studyMode.model.PracticeCardUiModel
import com.miszczyk.passlingo.ui.screens.studyMode.model.TypeAnswer
import com.miszczyk.passlingo.ui.screens.studyMode.quiz.model.QuizUiState
import com.miszczyk.passlingo.ui.screens.studyMode.viewmodel.BaseStudyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizViewModel(application: Application) : BaseStudyViewModel(application, StudyMode.QUIZ) {
    private val _uiState = MutableStateFlow(value = QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var allCards: List<StudyCardProgressEntity> = emptyList()

    override val logTag = "QuizViewModel"

    override fun onResetUiState() {
        _uiState.update {
            it.copy(
                isLoading = false, isBreather = false, userAnswer = TypeAnswer.NONE
            )
        }
    }

    override fun onHideBreatherState() {
        _uiState.update {
            it.copy(
                isBreather = false
            )
        }
    }

    override fun onShowErrorState(message: String) {
        _uiState.update {
            it.copy(
                errorMessage = message
            )
        }
    }

    override fun onSessionEndedUiState() {
        _uiState.update {
            it.copy(
                isLoading = false, currentFront = null, currentBack = null
            )
        }
    }

    override fun onPracticeCardsLoaded(cards: List<PracticeCardUiModel>) {
        _uiState.update {
            it.copy(
                cardsToPractice = cards
            )
        }
    }

    override suspend fun onSessionInitialized() {
        allCards = sessionRepository.getAllCards(currentSessionId)
    }

    override suspend fun showCurrentCard() {
        val currentProgress = currentBatch.firstOrNull() ?: return

        val flashcardData = deckDictionary[currentProgress.flashcardId]
        val correctAnswer = flashcardData?.second ?: "Unknown"
        val finished = sessionRepository.getFinishedCard(currentSessionId, targetRounds)

        val options = withContext(context = Dispatchers.Default) {
            val allMeanings = allCards.mapNotNull { progress ->
                deckDictionary[progress.flashcardId]?.second
            }.distinct()

            val allWrong = allMeanings.filter { !it.equals(other = correctAnswer, ignoreCase = true) }

            val wrongAnswers = if (allWrong.size <= 3) {
                allWrong
            } else {
                allWrong.sortedBy { levenshteinDistance(a = it, b = correctAnswer) }.take(n = 3)
            }
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

    fun checkUserAnswer(selectedText: String) {
        val isCorrect = selectedText == _uiState.value.currentBack

        if(isCorrect){
            val mediaPlayer = when {
                ((countPerfectAnswer+1) % 10) == 0-> R.raw.ten_correct_music
                ((countPerfectAnswer+1) % 5) == 0 -> R.raw.five_correct_music
                else -> R.raw.correct_music
            }
            playSound(mediaPlayer)
        }else{
            playSound(R.raw.wrong_music)
        }

        _uiState.update {
            it.copy(
                selectedAnswer = selectedText,
                userAnswer = if (isCorrect) TypeAnswer.GOOD else TypeAnswer.BAD
            )
        }
    }

    private fun levenshteinDistance(a: String, b: String): Int {
        val m = a.length
        val n = b.length
        var cost = IntArray(size = m + 1) { it }
        var newCost = IntArray(size = m + 1) { 0 }

        for (i in 1..n) {
            newCost[0] = i
            for (j in 1..m) {
                val match = if (a[j - 1] == b[i - 1]) 0 else 1
                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1
                newCost[j] = minOf(a = costInsert, b = costDelete, c = costReplace)
            }
            val swap = cost
            cost = newCost
            newCost = swap
        }
        return cost[m]
    }

    fun moveToNextCard() {
        viewModelScope.launch {
            val currentProgress = currentBatch.firstOrNull() ?: return@launch
            val isGoodAnswer = _uiState.value.userAnswer == TypeAnswer.GOOD

            runCatching {
                if (isGoodAnswer) {
                    countPerfectAnswer+=1
                    sessionRepository.incrementCurrentRound(
                        currentSessionId, currentProgress.flashcardId
                    )
                    if (currentProgress.currentRound + 1 == targetRounds) {
                        timeRepository.addCreditTime(secondsEarned = 10L * targetRounds)
                    }
                } else {
                    countPerfectAnswer = 0
                    sessionRepository.resetCurrentRound(
                        currentSessionId, currentProgress.flashcardId
                    )
                    sessionRepository.incrementAttempts(
                        currentSessionId, currentProgress.flashcardId
                    )
                }

                timeToBreath++
                currentBatch = currentBatch.drop(n = 1)

                when {
                    currentBatch.isNotEmpty() -> showCurrentCard()
                    timeToBreath >= 10 -> _uiState.update { it.copy(isBreather = true) }
                    else -> loadNextBatchAndShow()
                }
            }.onFailure {
                Log.e(logTag, "Failed to update card progress", it)
                onShowErrorState(getApplication<Application>().getString(R.string.error_study_session))
            }
        }
    }
}