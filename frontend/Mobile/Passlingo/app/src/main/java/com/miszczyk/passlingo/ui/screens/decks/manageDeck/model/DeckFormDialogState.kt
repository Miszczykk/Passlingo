package com.miszczyk.passlingo.ui.screens.decks.manageDeck.model

sealed interface DeckFormDialogState {
    data object None : DeckFormDialogState
    data object SaveDeckForm : DeckFormDialogState
    data object DiscardChanges : DeckFormDialogState
    data class Error(val message: String) : DeckFormDialogState

    data class EditFlashcard(val id: String, val frontText: String, val backText: String) : DeckFormDialogState

    data class DeleteFlashcard(val id: String, val frontText: String, val backText: String) : DeckFormDialogState
}