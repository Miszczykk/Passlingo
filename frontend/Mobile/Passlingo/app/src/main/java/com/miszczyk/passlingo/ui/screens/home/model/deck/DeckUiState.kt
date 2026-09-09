package com.miszczyk.passlingo.ui.screens.home.model.deck

import com.miszczyk.passlingo.data.local.entity.DeckWithFlashcards

data class DeckUiState(
    val deckDialogState: DeckDialogState = DeckDialogState.None,
    val deckBottomSheetState: DeckBottomSheetState = DeckBottomSheetState.None,

    val decks: List<DeckWithFlashcards> = emptyList(),
    val selectedDeckId: String? = null,
)
