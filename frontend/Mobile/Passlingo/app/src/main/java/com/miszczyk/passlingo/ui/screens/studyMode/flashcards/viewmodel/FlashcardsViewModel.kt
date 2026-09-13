package com.miszczyk.passlingo.ui.screens.studyMode.flashcards.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.data.local.entity.StudyMode
import com.miszczyk.passlingo.ui.screens.studyMode.flashcards.model.FlashcardsUiState
import com.miszczyk.passlingo.ui.screens.studyMode.model.PracticeCardUiModel
import com.miszczyk.passlingo.ui.screens.studyMode.viewmodel.BaseStudyViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlashcardsViewModel(application: Application) :
    BaseStudyViewModel(application, StudyMode.FLASHCARDS) {
    private val _uiState = MutableStateFlow(FlashcardsUiState())
    val uiState: StateFlow<FlashcardsUiState> = _uiState.asStateFlow()

    override val logTag = "FlashcardsViewModel"

    override fun onResetUiState() {
        _uiState.update {
            it.copy(
                isLoading = false, isBreather = false, isFlipped = false
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

    override suspend fun showCurrentCard() {
        if (currentBatch.isEmpty()) return

        val currentProgress = currentBatch.first()
        val flashcardData = deckDictionary[currentProgress.flashcardId]

        val finished = sessionRepository.getFinishedCard(currentSessionId, targetRounds)

        _uiState.update {
            it.copy(
                isLoading = false,
                isFlipped = false,
                currentFront = flashcardData?.first ?: "Unknown",
                currentBack = flashcardData?.second ?: "Unknown",
                progressText = "$finished / $totalCardsInSession"
            )
        }
    }

    fun flipCard() {
        _uiState.update {
            it.copy(
                isFlipped = !it.isFlipped
            )
        }
    }

    fun answerCard(isCorrect: Boolean) {
        if (currentBatch.isEmpty()) return

        val currentProgress = currentBatch.first()
        timeToBreath++

        viewModelScope.launch {
            runCatching {
                if (isCorrect) {
                    sessionRepository.incrementCurrentRound(
                        currentSessionId, currentProgress.flashcardId
                    )
                    if (currentProgress.currentRound + 1 == targetRounds) {
                        timeRepository.addCreditTime(secondsEarned = (10L * targetRounds))
                    }
                } else {
                    sessionRepository.resetCurrentRound(
                        currentSessionId, currentProgress.flashcardId
                    )
                    sessionRepository.incrementAttempts(
                        currentSessionId, currentProgress.flashcardId
                    )
                }

                currentBatch = currentBatch.drop(1)

                when {
                    currentBatch.isNotEmpty() -> showCurrentCard()
                    timeToBreath >= 10 -> _uiState.update { it.copy(isBreather = true) }
                    else -> loadNextBatchAndShow()
                }
            }.onFailure { error ->
                Log.e(logTag, "Failed to process card answer", error)
                onShowErrorState(getApplication<Application>().getString(R.string.error_study_session))
            }
        }
    }
}