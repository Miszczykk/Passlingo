package com.miszczyk.passlingo.ui.screens.studyMode.typing.model

import androidx.compose.foundation.text.input.TextFieldState
import com.miszczyk.passlingo.ui.screens.studyMode.model.PracticeCardUiModel
enum class TypeAnswer {NONE, GOOD, BAD}
data class TypingUiState (
    val isLoading: Boolean = true,
    val isBreather: Boolean = false,
    val currentFront: String? = null,
    val currentBack: String? = null,
    val progressText: String = "0 / 0",
    val cardsToPractice: List<PracticeCardUiModel> = emptyList(),
    val userAnswer: TypeAnswer = TypeAnswer.NONE,
    val userAnswerState: TextFieldState = TextFieldState(initialText = ""),

    val errorMessage: String? = null
)