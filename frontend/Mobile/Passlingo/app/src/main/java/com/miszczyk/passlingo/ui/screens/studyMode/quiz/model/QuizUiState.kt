package com.miszczyk.passlingo.ui.screens.studyMode.quiz.model

import com.miszczyk.passlingo.ui.screens.studyMode.model.PracticeCardUiModel

enum class TypeAnswer {NONE, GOOD, BAD}

data class QuizUiState(
    val isLoading: Boolean = true,
    val isBreather: Boolean = false,
    val currentFront: String? = null,
    val currentBack: String? = null,
    val progressText: String = "0 / 0",
    val cardsToPractice: List<PracticeCardUiModel> = emptyList(),
    val options: List<String> = emptyList(),
    val selectedAnswer: String? = null,
    val userAnswer: TypeAnswer = TypeAnswer.NONE,

    val errorMessage: String? = null,
)