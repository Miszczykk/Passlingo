package com.miszczyk.passlingo.ui.screens.decks.editDeck.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.DeckFormDialogState
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.Flashcard
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.viewmodel.DeckFormViewModel
import com.miszczyk.passlingo.ui.util.DeckIcons
import com.miszczyk.passlingo.ui.util.setText
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditDeckViewModel(application: Application) : DeckFormViewModel(application){
    private var currentDeckId: String = ""


    fun loadDeckData(deckId: String) {
        if (currentDeckId == deckId) return
        currentDeckId = deckId

        viewModelScope.launch {
            runCatching {
                deckRepository.getDeckWithFlashcardsById(deckId)
            }.fold(
                onSuccess = { deckData ->
                    if (deckData != null) {
                        deckName.setText(deckData.deck.name)
                        val uiFlashcards = deckData.flashcards.map {
                            Flashcard(id = it.id, front = it.front, back = it.back)
                        }
                        _uiState.update {
                            it.copy(
                                deckIcon = DeckIcons.findIconFromId(deckData.deck.iconResId),
                                cards = uiFlashcards
                            )
                        }
                    } else{
                        Log.w("EditDeckViewModel", "Deck with id $deckId not found (returned null)")
                        _uiState.update {
                            it.copy(
                                dialogState = DeckFormDialogState.Error(
                                    getApplication<Application>().getString(R.string.error_deck_not_found)
                                )
                            )
                        }
                    }
                },
                onFailure = { error ->
                    Log.e("EditDeckViewModel", "Failed to load deck data", error)
                    _uiState.update {
                        it.copy(
                            dialogState = DeckFormDialogState.Error(
                                error.localizedMessage ?: getApplication<Application>().getString(R.string.error_deck_not_found)
                            )
                        )
                    }
                }
            )
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