package com.miszczyk.passlingo.ui.screens.createDeck.viewmodel

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.data.repository.DeckRepository
import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckDialogState
import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckUiState
import com.miszczyk.passlingo.ui.screens.createDeck.model.Flashcard
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class CreateDeckViewModel(application: Application) : AndroidViewModel(application) {
    private val deckRepository = DeckRepository(context = application)
    private val _uiState = MutableStateFlow(value = CreateDeckUiState())
    val uiState: StateFlow<CreateDeckUiState> = _uiState.asStateFlow()

    private val _navigateBack = Channel<Unit>(capacity = Channel.BUFFERED)
    val navigateBack = _navigateBack.receiveAsFlow()

    private val dialogAction = CreateDeckDialogAction(
        uiStateFlow = _uiState,
        externalScope = viewModelScope,
        navigateBack = _navigateBack,
        saveDeck = { saveDeckToDatabase() },
        clearScreen = { clearScreen() },
        onEditCardConfirmed = { id ->
            editCard(
                id = id,
                newFrontText = editFrontState.text.toString(),
                newBackText = editBackState.text.toString()
            )
        },
        onDeleteConfirmed = { id -> deleteCard(id) }
    )

    val deckName: TextFieldState = TextFieldState(initialText = "")
    val frontCreateCardState: TextFieldState = TextFieldState(initialText = "")
    val backCreateCardState: TextFieldState = TextFieldState(initialText = "")

    val editFrontState: TextFieldState = TextFieldState(initialText = "")
    val editBackState: TextFieldState = TextFieldState(initialText = "")

    private suspend fun saveDeckToDatabase() {
        deckRepository.saveDeck(
            name = deckName.text.toString(),
            iconResId = _uiState.value.deckIcon,
            cards = _uiState.value.cards
        )
    }


    fun onDialogCancelled() = dialogAction.onDialogCancelled()
    fun onDialogConfirmed() = dialogAction.onDialogConfirmed()
    fun onBack() = dialogAction.onDiscardDialogConfirmed()

    fun onSaveDeckClicked() {
        if (deckName.text.toString().isNotBlank() && _uiState.value.cards.isNotEmpty()) {
            _uiState.update { it.copy(dialogState = CreateDeckDialogState.SaveDeck) }
        }
    }

    fun onBackClicked() {
        if (deckName.text.toString().isNotBlank() || _uiState.value.cards.isNotEmpty()) {
            _uiState.update { it.copy(dialogState = CreateDeckDialogState.DiscardChanges) }
        } else {
            onBack()
        }
    }

    fun onSelectIconClicked() {
        _uiState.update { it.copy(showBottomSheet = true) }
    }

    fun onIconClicked(selectedIcon: Int) {
        _uiState.update { it.copy(deckIcon = selectedIcon) }
        onSheetDismissed()
    }

    fun onAddToDeckClicked() {
        val frontText = frontCreateCardState.text.toString()
        val backText = backCreateCardState.text.toString()
        if (frontCreateCardState.text.toString().isNotBlank() && backCreateCardState.text.toString()
                .isNotBlank()
        ) {
            val newCard = Flashcard(front = frontText, back = backText)
            _uiState.update { currentState ->
                currentState.copy(
                    cards = currentState.cards + newCard
                )
            }
            frontCreateCardState.edit { replace(start = 0, end = length, text = "") }
            backCreateCardState.edit { replace(start = 0, end = length, text = "") }
        } else {
            _uiState.update {
                it.copy(
                    dialogState = CreateDeckDialogState.Error(
                        message = getApplication<Application>().getString(
                            R.string.dialog_message_incomplete_flashcards
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
                dialogState = CreateDeckDialogState.EditFlashcard(
                    id = card.id, frontText = card.front, backText = card.back
                )
            )
        }
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

    fun onDeleteCardClicked(card: Flashcard) {
        _uiState.update {
            it.copy(
                dialogState = CreateDeckDialogState.DeleteFlashcard(
                    id = card.id, frontText = card.front, backText = card.back
                )
            )
        }
    }

    private fun deleteCard(id: String) {
        _uiState.update { state ->
            state.copy(cards = state.cards.filterNot { it.id == id })
        }
    }

    fun onSheetDismissed() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }

    fun clearScreen() {
        _uiState.update { it.copy(cards = emptyList()) }
        deckName.edit { replace(start = 0, end = length, text = "") }
        frontCreateCardState.edit { replace(start = 0, end = length, text = "") }
        backCreateCardState.edit { replace(start = 0, end = length, text = "") }
    }
}