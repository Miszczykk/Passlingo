package com.miszczyk.passlingo.ui.screens.createDeck.model

sealed interface CreateDeckDialogState {
    data object None : CreateDeckDialogState
    data object SaveDeck : CreateDeckDialogState
    data object DiscardChanges : CreateDeckDialogState
    data class Error(val message: String) : CreateDeckDialogState

    data class EditFlashcard(val id: String, val frontText: String, val backText: String) : CreateDeckDialogState

    data class DeleteFlashcard(val id: String, val frontText: String, val backText: String) : CreateDeckDialogState
}