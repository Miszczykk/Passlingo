package com.miszczyk.passlingo.ui.screens.studyMode.flashcards.model

import com.miszczyk.passlingo.ui.screens.studyMode.model.BaseStudyUiState
import com.miszczyk.passlingo.ui.screens.studyMode.model.PracticeCardUiModel

data class FlashcardsUiState(
    override val isLoading: Boolean = true,
    override val isBreather: Boolean = false,
    override val currentFront: String? = null,
    override val currentBack: String? = null,
    override val progressText: String = "0 / 0",
    override val cardsToPractice: List<PracticeCardUiModel> = emptyList(),
    override val errorMessage: String? = null,

    val isFlipped: Boolean = false
) : BaseStudyUiState