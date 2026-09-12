package com.miszczyk.passlingo.ui.screens.studyMode.flashcards.model

data class FlashcardsUiState(
    val isLoading: Boolean = true,
    val isFlipped: Boolean = false,
    val isBreather: Boolean = false,
    val currentFront: String? = null,
    val currentBack: String? = null,
    val progressText: String = "0 / 0",
    val cardsToPractice: List<PracticeCardUiModel> = emptyList(),

    val errorMessage: String? = null
)