package com.miszczyk.passlingo.ui.screens.createDeck.model
sealed interface CreateDeckDialogState{
    data object None : CreateDeckDialogState
    data object SaveDeck : CreateDeckDialogState
    data object DiscardChanges : CreateDeckDialogState
    data class Error(val message: String) : CreateDeckDialogState
}