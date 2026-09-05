package com.miszczyk.passlingo.ui.screens.home.model.deck

sealed interface HasDeckName {
    val deckName: String
}

sealed interface DeckDialogState {
    data object None : DeckDialogState
    data class ConfirmDelete(override val deckName: String) : DeckDialogState, HasDeckName
    data class Error(val message: String) : DeckDialogState
}