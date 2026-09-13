package com.miszczyk.passlingo.ui.screens.studyMode.model

interface BaseStudyUiState{
    val isLoading: Boolean
    val isBreather: Boolean
    val currentFront: String?
    val currentBack: String?
    val progressText: String
    val cardsToPractice: List<PracticeCardUiModel>
    val errorMessage: String?
}
