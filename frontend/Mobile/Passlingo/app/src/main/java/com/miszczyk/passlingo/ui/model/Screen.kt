package com.miszczyk.passlingo.ui.model

sealed interface Screen {
    data object Loading : Screen
    data object Home : Screen
    data object CreateDeck : Screen
    data class EditDeck(val deckId: String) : Screen
}