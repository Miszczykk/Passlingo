package com.miszczyk.passlingo.ui.screens.createDeck.model

import com.miszczyk.passlingo.R

data class CreateDeckUiState(
    val dialogState: CreateDeckDialogState = CreateDeckDialogState.None,
    val showBottomSheet: Boolean = false,

    val deckIcon: Int = R.drawable.deck_animal_bear,

    val cards: List<Flashcard> = emptyList()
)
