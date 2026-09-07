package com.miszczyk.passlingo.ui.screens.decks.editDeck.viewmodel

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.data.repository.DeckRepository
import com.miszczyk.passlingo.ui.screens.decks.createDeck.viewmodel.DeckFormDialogAction
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.DeckFormDialogState
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.DeckFormUiState
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.Flashcard
import com.miszczyk.passlingo.ui.util.DeckIcons
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditDeckViewModel(application: Application) : AndroidViewModel(application){
    private val deckRepository = DeckRepository(context = application)
    private val _uiState = MutableStateFlow(value = DeckFormUiState())
    val uiState: StateFlow<DeckFormUiState> = _uiState.asStateFlow()
    private val _navigateBack = Channel<Unit>(capacity = Channel.BUFFERED)
    val navigateBack = _navigateBack.receiveAsFlow()

    private var currentDeckId: String = ""

    val deckName: TextFieldState = TextFieldState(initialText = "")
    val frontCreateCardState: TextFieldState = TextFieldState(initialText = "")
    val backCreateCardState: TextFieldState = TextFieldState(initialText = "")
    val editFrontState: TextFieldState = TextFieldState(initialText = "")
    val editBackState: TextFieldState = TextFieldState(initialText = "")

    private val dialogAction = DeckFormDialogAction(
        uiStateFlow = _uiState,
        externalScope = viewModelScope,
        navigateBack = _navigateBack,
        saveDeck = { saveDeckToDatabase() },
        clearScreen = { viewModelScope.launch { clearScreen() }},
        onEditCardConfirmed = { id ->
            editCard(
                id = id,
                newFrontText = editFrontState.text.toString(),
                newBackText = editBackState.text.toString()
            )
        },
        onDeleteConfirmed = {id -> deleteCard(id)}
    )

    fun loadDeckData(deckId: String) {
        if (currentDeckId == deckId) return
        currentDeckId = deckId

        viewModelScope.launch {
            val deckData = deckRepository.getDeckWithFlashcardsById(deckId)
            if (deckData != null) {
                deckName.edit { replace(0, length, deckData.deck.name) }
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

    private suspend fun saveDeckToDatabase() {
        deckRepository.updateDeck(
            id = currentDeckId,
            name = deckName.text.toString(),
            iconResId = _uiState.value.deckIcon.id,
            cards = _uiState.value.cards
        )
    }

    private fun editCard(id: String, newFrontText: String, newBackText: String) {
        _uiState.update { state ->
            state.copy(
                cards = state.cards.map { card ->
                    if (card.id == id) {
                        card.copy(front = newFrontText, back = newBackText)
                    } else {
                        card
                    }
                }
            )
        }
    }

    private fun deleteCard(id: String) {
        _uiState.update { state ->
            state.copy(cards = state.cards.filterNot { it.id == id })
        }
    }

    fun clearScreen() {
        frontCreateCardState.edit { replace(start = 0, end = length, text = "") }
        backCreateCardState.edit { replace(start = 0, end = length, text = "") }
    }

    fun onDialogCancelled() = dialogAction.onDialogCancelled()
    fun onDialogConfirmed() = dialogAction.onDialogConfirmed()
    fun onBack() = dialogAction.onDiscardDialogConfirmed()

    fun onSaveDeckClicked() {
        if (deckName.text.toString().isNotBlank() && _uiState.value.cards.isNotEmpty()) {
            _uiState.update { it.copy(dialogState = DeckFormDialogState.SaveDeckForm) }
        }
    }

    fun onBackClicked() {
        if (deckName.text.toString().isNotBlank() || _uiState.value.cards.isNotEmpty()) {
            _uiState.update { it.copy(dialogState = DeckFormDialogState.DiscardChanges) }
        } else {
            onBack()
        }
    }

    fun onSelectIconClicked() {
        _uiState.update { it.copy(showBottomSheet = true) }
    }

    fun onIconClicked(selectedIcon: DeckIcons) {
        _uiState.update { it.copy(deckIcon = selectedIcon) }
        onSheetDismissed()
    }

    fun onAddToDeckClicked() {
        val frontText = frontCreateCardState.text.toString()
        val backText = backCreateCardState.text.toString()
        if (frontText.isNotBlank() && backText.isNotBlank()) {
            val newCard = Flashcard(front = frontText, back = backText)
            _uiState.update { currentState ->
                currentState.copy(cards = currentState.cards + newCard)
            }
            frontCreateCardState.edit { replace(start = 0, end = length, text = "") }
            backCreateCardState.edit { replace(start = 0, end = length, text = "") }
        } else {
            _uiState.update {
                it.copy(
                    dialogState = DeckFormDialogState.Error(
                        message = getApplication<Application>().getString(
                            com.miszczyk.passlingo.R.string.dialog_message_incomplete_flashcards
                        )
                    )
                )
            }
        }
    }

    fun onEditCardClicked(card: Flashcard) {
        editFrontState.edit { replace(start = 0, end = length, text = card.front) }
        editBackState.edit { replace(start = 0, end = length, text = card.back) }

        _uiState.update {
            it.copy(
                dialogState = DeckFormDialogState.EditFlashcard(
                    id = card.id, frontText = card.front, backText = card.back
                )
            )
        }
    }

    fun onDeleteCardClicked(card: Flashcard) {
        _uiState.update {
            it.copy(
                dialogState = DeckFormDialogState.DeleteFlashcard(
                    id = card.id, frontText = card.front, backText = card.back
                )
            )
        }
    }

    fun onSheetDismissed() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }
}