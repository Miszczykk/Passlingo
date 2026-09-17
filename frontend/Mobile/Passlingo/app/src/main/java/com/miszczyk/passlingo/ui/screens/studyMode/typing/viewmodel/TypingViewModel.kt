package com.miszczyk.passlingo.ui.screens.studyMode.typing.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.BuildConfig
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.data.ai.GeminiAnswerVerifier
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

class TypingViewModel(application: Application) :
    BaseStudyViewModel(application, StudyMode.TYPING) {
    private val _uiState = MutableStateFlow(value = TypingUiState())
    val uiState: StateFlow<TypingUiState> = _uiState.asStateFlow()

    private var repeatCard: StudyCardProgressEntity? = null

    override val logTag = "TypingViewModel"

    override fun onResetUiState() {
        _uiState.update {
            it.copy(
                isLoading = false,
                isBreather = false,
                userAnswer = TypeAnswer.NONE,
                userAnswerState = TextFieldState(initialText = ""),
                hasAiRejected = false,
                isAiChecking = false
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

    fun checkUserAnswer(userAnswer: String, correctAnswer: String?) {
        val cleanUser = userAnswer.trim().replace(regex = "\\s+".toRegex(), replacement = " ")
        val cleanCorrect = correctAnswer?.trim()?.replace(regex = "\\s+".toRegex(), replacement = " ") ?: ""

        if (cleanUser.equals(other = cleanCorrect, ignoreCase = true)) {
            val mediaPlayer = when {
                ((countPerfectAnswer+1) % 10) == 0-> R.raw.ten_correct_music
                ((countPerfectAnswer+1) % 5) == 0 -> R.raw.five_correct_music
                else -> R.raw.correct_music
            }

            _uiState.update { it.copy(userAnswer = TypeAnswer.GOOD) }
            playSound(mediaPlayer)
            timeToBreath++
        } else {
            playSound(R.raw.wrong_music)

            _uiState.update { it.copy(userAnswer = TypeAnswer.BAD) }
            if (repeatCard == null) {
                repeatCard = currentBatch.firstOrNull()
            }
        }
    }

    fun moveToNextCard() {
        if (_uiState.value.isAiChecking) return
        viewModelScope.launch {
            val currentProgress = repeatCard ?: currentBatch.firstOrNull() ?: return@launch
            val isGoodAnswer = _uiState.value.userAnswer == TypeAnswer.GOOD

            runCatching {
                if (isGoodAnswer) {
                    countPerfectAnswer+=1
                    if (repeatCard != null) {
                        repeatCard = null
                        _uiState.update { it.copy(hasAiRejected = false, isAiChecking = false) }
                    } else {
                        sessionRepository.incrementCurrentRound(
                            currentSessionId, currentProgress.flashcardId
                        )
                        if (currentProgress.currentRound + 1 == targetRounds) {
                            timeRepository.addCreditTime(secondsEarned = (10L * targetRounds))
                        }
                        currentBatch = currentBatch.drop(n = 1)
                    }
                } else {
                    countPerfectAnswer = 0
                    sessionRepository.resetCurrentRound(
                        currentSessionId, currentProgress.flashcardId
                    )
                    sessionRepository.incrementAttempts(
                        currentSessionId, currentProgress.flashcardId
                    )

                    if (currentBatch.firstOrNull()?.id == currentProgress.id) {
                        currentBatch = currentBatch.drop(n = 1)
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

    private val answerVerifier = GeminiAnswerVerifier(apiKey = BuildConfig.GEMINI_API_KEY)

    fun checkAgain(userAnswer: TextFieldState, correctAnswer: String?) {
        if (correctAnswer == null) return
        if (_uiState.value.isAiChecking) return

        viewModelScope.launch {
            _uiState.update { it.copy(
                isAiChecking = true)
            }
            answerVerifier.verify(
                front = _uiState.value.currentFront ?: "",
                expectedAnswer = correctAnswer,
                userAnswer = userAnswer.text.toString()
            ).onSuccess { verdict ->
                if(verdict.isCorrect){
                    checkUserAnswer(correctAnswer, correctAnswer)
                    repeatCard = null
                    _uiState.update { it.copy(isAiChecking = false) }
                }else{
                    _uiState.update { it.copy(
                        isAiChecking = false,
                        hasAiRejected = true,
                        aiExplanationDialogText = verdict.explanation
                    ) }
                }
            }.onFailure { e ->
                Log.e(logTag, "Gemini verification failed", e)
                _uiState.update {
                    it.copy(
                        hasAiError = true,
                        isAiChecking = false,
                        hasAiRejected = true,
                        errorMessage = getApplication<Application>()
                            .getString(R.string.error_study_session)
                    )
                }
            }
        }
    }

    fun confirmButtonAIExplanationDialog(){
        repeatCard = null
        _uiState.update { it.copy(aiExplanationDialogText = null, isAiChecking = false, hasAiRejected = false, hasAiError = false) }
        checkUserAnswer("a", "a")
    }
    fun dismissButtonAIExplanationDialog(){
        _uiState.update { it.copy(aiExplanationDialogText = null, hasAiError = false) }
    }
}