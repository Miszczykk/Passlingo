package com.miszczyk.passlingo.ui.screens.studyMode.typing.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.data.local.entity.StudyCardProgressEntity
import com.miszczyk.passlingo.data.local.entity.StudyMode
import com.miszczyk.passlingo.ui.screens.studyMode.model.PracticeCardUiModel
import com.miszczyk.passlingo.ui.screens.studyMode.model.TypeAnswer
import com.miszczyk.passlingo.ui.screens.studyMode.typing.model.TypingUiState
import com.miszczyk.passlingo.ui.screens.studyMode.viewmodel.BaseStudyViewModel
import com.miszczyk.passlingo.ui.util.clear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TypingViewModel(application: Application) : BaseStudyViewModel(application, StudyMode.TYPING) {
    private val _uiState = MutableStateFlow(TypingUiState())
    val uiState: StateFlow<TypingUiState> = _uiState.asStateFlow()

    private var repeatCard: StudyCardProgressEntity? = null

    override val logTag = "TypingViewModel"

    override fun onResetUiState() {
        _uiState.update {
            it.copy(
                isLoading = false,
                isBreather = false,
                userAnswer = TypeAnswer.NONE,
                userAnswerState = TextFieldState(initialText = "")
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
                isLoading = false,
                currentFront = null,
                currentBack = null
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
        val currentProgress = repeatCard ?: currentBatch.firstOrNull() ?: return

        val typingData = deckDictionary[currentProgress.flashcardId]
        val finished = sessionRepository.getFinishedCard(currentSessionId, targetRounds)

        _uiState.update {
            it.copy(
                isLoading = false,
                currentFront = typingData?.first ?: "Unknown",
                currentBack = typingData?.second ?: "Unknown",
                progressText = "$finished / $totalCardsInSession"
            )
        }
    }

    fun checkUserAnswer(userAnswer: TextFieldState, correctAnswer: String?){
        val cleanUser = userAnswer.text.toString()
            .trim()
            .replace("\\s+".toRegex(), " ")

        val cleanCorrect = correctAnswer
            ?.trim()
            ?.replace("\\s+".toRegex(), " ")
            ?: ""

        if (cleanUser.equals(cleanCorrect, ignoreCase = true)) {
            _uiState.update { it.copy(userAnswer = TypeAnswer.GOOD) }
            timeToBreath++
        } else {
            _uiState.update { it.copy(userAnswer = TypeAnswer.BAD) }
            if (repeatCard == null) {
                repeatCard = currentBatch.firstOrNull()
            }
        }
    }

    fun moveToNextCard(){
        viewModelScope.launch {
            val currentProgress = repeatCard ?: currentBatch.firstOrNull() ?: return@launch
            val isGoodAnswer = _uiState.value.userAnswer == TypeAnswer.GOOD

            runCatching {
                if(isGoodAnswer){
                    if (repeatCard != null) {
                        repeatCard = null
                    } else {
                        sessionRepository.incrementCurrentRound(currentSessionId, currentProgress.flashcardId)
                        if (currentProgress.currentRound + 1 == targetRounds) {
                            timeRepository.addCreditTime(secondsEarned = (10L * targetRounds))
                        }
                        currentBatch = currentBatch.drop(1)
                    }
                }else{
                    sessionRepository.resetCurrentRound(currentSessionId, currentProgress.flashcardId)
                    sessionRepository.incrementAttempts(currentSessionId, currentProgress.flashcardId)

                    if (currentBatch.firstOrNull()?.id == currentProgress.id) {
                        currentBatch = currentBatch.drop(1)
                    }
                }

                _uiState.update { it.copy(userAnswer = TypeAnswer.NONE) }

                when {
                    currentBatch.isNotEmpty() || repeatCard != null -> showCurrentCard()
                    timeToBreath >= 10 -> _uiState.update { it.copy(isBreather = true) }
                    else -> loadNextBatchAndShow()
                }
            }.onFailure {
                Log.e(logTag, "Failed to update card progress", it)
                onShowErrorState(getApplication<Application>().getString(R.string.error_study_session))
            }
            _uiState.value.userAnswerState.clear()
        }
    }
    fun checkAgain(userAnswer: TextFieldState, correctAnswer: String?) {
    //TODO implement AI
}
}