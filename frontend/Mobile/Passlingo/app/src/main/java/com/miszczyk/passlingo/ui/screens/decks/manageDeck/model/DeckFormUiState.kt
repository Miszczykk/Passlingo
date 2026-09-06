package com.miszczyk.passlingo.ui.screens.decks.manageDeck.model

import com.miszczyk.passlingo.R

data class DeckFormUiState(
    val dialogState: DeckFormDialogState = DeckFormDialogState.None,
    val showBottomSheet: Boolean = false,

    val deckIcon: Int = R.drawable.deck_animal_bear,

    val cards: List<Flashcard> = emptyList()
)
