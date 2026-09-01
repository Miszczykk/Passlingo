package com.miszczyk.passlingo.ui.screens.createDeck.model

import androidx.compose.foundation.text.input.TextFieldState
import com.miszczyk.passlingo.R

data class CreateDeckUiState(
    val dialogState: CreateDeckDialogState = CreateDeckDialogState.None,
    val showBottomSheet: Boolean = false,

    val deckName: TextFieldState = TextFieldState(""),
    val deckIcon: Int = R.drawable.deck_animal_bear,

    val addedCards: Int = 0
)
