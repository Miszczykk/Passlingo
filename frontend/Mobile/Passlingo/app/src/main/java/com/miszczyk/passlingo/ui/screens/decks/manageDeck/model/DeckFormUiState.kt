package com.miszczyk.passlingo.ui.screens.decks.manageDeck.model

import com.miszczyk.passlingo.ui.util.DeckIcons

data class DeckFormUiState(
    val dialogState: DeckFormDialogState = DeckFormDialogState.None,
    val showBottomSheet: Boolean = false,

    val deckIcon: DeckIcons = DeckIcons.BEAR,

    val cards: List<Flashcard> = emptyList()
)
