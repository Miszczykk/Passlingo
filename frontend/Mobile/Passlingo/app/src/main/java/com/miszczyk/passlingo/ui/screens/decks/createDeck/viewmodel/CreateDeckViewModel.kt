package com.miszczyk.passlingo.ui.screens.decks.createDeck.viewmodel

import android.app.Application
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.viewmodel.DeckFormViewModel
import kotlinx.coroutines.flow.update

class CreateDeckViewModel(application: Application) : DeckFormViewModel(application) {
    override suspend fun saveDeckToDatabase() {
        deckRepository.saveDeck(
            name = deckName.text.toString(),
            iconResId = _uiState.value.deckIcon.id,
            cards = _uiState.value.cards
        )
    }

    override fun clearScreen() {
        super.clearScreen()
        _uiState.update { it.copy(cards = emptyList()) }
        deckName.edit { replace(start = 0, end = length, text = "") }
    }
}