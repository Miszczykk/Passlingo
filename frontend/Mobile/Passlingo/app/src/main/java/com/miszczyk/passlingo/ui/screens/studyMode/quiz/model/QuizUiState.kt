package com.miszczyk.passlingo.ui.screens.studyMode.quiz.model

import com.miszczyk.passlingo.ui.screens.studyMode.model.BaseStudyUiState
import com.miszczyk.passlingo.ui.screens.studyMode.model.PracticeCardUiModel
import com.miszczyk.passlingo.ui.screens.studyMode.model.TypeAnswer


data class QuizUiState(
    override val isLoading: Boolean = true,
    override val isBreather: Boolean = false,
    override val currentFront: String? = null,
    override val currentBack: String? = null,
    override val progressText: String = "0 / 0",
    override val cardsToPractice: List<PracticeCardUiModel> = emptyList(),
    override val errorMessage: String? = null,

    val options: List<String> = emptyList(),
    val selectedAnswer: String? = null,
    val userAnswer: TypeAnswer = TypeAnswer.NONE
) : BaseStudyUiState