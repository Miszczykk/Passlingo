package com.miszczyk.passlingo.ui.screens.home.viewmodel.deck

import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckDialogState
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DeckSelectionAction(private val uiStateFlow: MutableStateFlow<DeckUiState>) {

    fun onDeleteDeckClicked() {
        uiStateFlow.update { state ->
            val deckId = state.selectedDeckId
            if (deckId != null) {
                val deckName = state.decks.find { it.deck.id == deckId }?.deck?.name ?: "Unknown Deck"
                state.copy(deckDialogState = DeckDialogState.ConfirmDelete(deckName))
            } else {
                state
            }
        }
    }
}