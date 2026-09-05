package com.miszczyk.passlingo.ui.screens.home.model.deck

import com.miszczyk.passlingo.data.local.entity.DeckWithFlashcards

data class DeckUiState(
    val showBottomSheet: Boolean = false,
    val deckDialogState: DeckDialogState = DeckDialogState.None,

    val decks: List<DeckWithFlashcards> = emptyList(),
    val selectedDeckId: String? = null,
)
