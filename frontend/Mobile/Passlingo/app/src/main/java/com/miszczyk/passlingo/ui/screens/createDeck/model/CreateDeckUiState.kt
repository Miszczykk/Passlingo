package com.miszczyk.passlingo.ui.screens.createDeck.model


data class CreateDeckUiState(
    val dialogState: CreateDeckDialogState = CreateDeckDialogState.None,
    val showBottomSheet: Boolean = false,

    val deckName: String = "",
    val deckIcon: String = "",

    val addedCards: Int = 1
)
