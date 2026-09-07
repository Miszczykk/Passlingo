package com.miszczyk.passlingo.ui.screens.decks.editDeck.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.Flashcard
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.viewmodel.DeckFormViewModel
import com.miszczyk.passlingo.ui.util.DeckIcons
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditDeckViewModel(application: Application) : DeckFormViewModel(application){
    private var currentDeckId: String = ""


    fun loadDeckData(deckId: String) {
        if (currentDeckId == deckId) return
        currentDeckId = deckId

        viewModelScope.launch {
            val deckData = deckRepository.getDeckWithFlashcardsById(deckId)
            if (deckData != null) {
                deckName.edit { replace(start = 0, end = length, text = deckData.deck.name) }
                val uiFlashcards = deckData.flashcards.map {
                    Flashcard(id = it.id, front = it.front, back = it.back)
                }
                _uiState.update {
                    it.copy(
                        deckIcon = DeckIcons.findIconFromId(deckData.deck.iconResId),
                        cards = uiFlashcards
                    )
                }
            }
        }
    }

    override suspend fun saveDeckToDatabase() {
        deckRepository.updateDeck(
            id = currentDeckId,
            name = deckName.text.toString(),
            iconResId = _uiState.value.deckIcon.id,
            cards = _uiState.value.cards
        )
    }
}