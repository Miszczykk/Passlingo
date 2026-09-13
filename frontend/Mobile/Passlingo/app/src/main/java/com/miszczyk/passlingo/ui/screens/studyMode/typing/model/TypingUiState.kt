package com.miszczyk.passlingo.ui.screens.studyMode.typing.model

import androidx.compose.foundation.text.input.TextFieldState
import com.miszczyk.passlingo.ui.screens.studyMode.model.BaseStudyUiState
import com.miszczyk.passlingo.ui.screens.studyMode.model.PracticeCardUiModel
import com.miszczyk.passlingo.ui.screens.studyMode.model.TypeAnswer

data class TypingUiState (
    override val isLoading: Boolean = true,
    override val isBreather: Boolean = false,
    override val currentFront: String? = null,
    override val currentBack: String? = null,
    override val progressText: String = "0 / 0",
    override val cardsToPractice: List<PracticeCardUiModel> = emptyList(),
    override val errorMessage: String? = null,

    val userAnswer: TypeAnswer = TypeAnswer.NONE,
    val userAnswerState: TextFieldState = TextFieldState(initialText = ""),
) : BaseStudyUiState