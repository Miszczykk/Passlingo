package com.miszczyk.passlingo.ui.screens.decks.manageDeck.viewmodel

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.data.repository.DeckRepository
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.DeckFormDialogState
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.DeckFormUiState
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.Flashcard
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.util.DeckFormConstants.MIN_CARDS_REQUIRES
import com.miszczyk.passlingo.ui.util.DeckIcons
import com.miszczyk.passlingo.ui.util.clear
import com.miszczyk.passlingo.ui.util.setText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class DeckFormViewModel(application: Application) : AndroidViewModel(application) {
    protected val deckRepository = DeckRepository(context = application)

    protected val _uiState = MutableStateFlow(value = DeckFormUiState())
    val uiState: StateFlow<DeckFormUiState> = _uiState.asStateFlow()

    protected val _navigateBack = Channel<Unit>(capacity = Channel.BUFFERED)
    val navigateBack = _navigateBack.receiveAsFlow()

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

    protected abstract suspend fun saveDeckToDatabase()

    open fun clearScreen(){
        frontCreateCardState.clear()
        backCreateCardState.clear()
    }

    fun onSheetDismissed() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }


    fun onDialogCancelled() = dialogAction.onDialogCancelled()
    fun onDialogConfirmed() = dialogAction.onDialogConfirmed()
    fun onBack() = dialogAction.onDiscardDialogConfirmed()


    fun onSaveDeckClicked(){
        if (deckName.text.toString().isNotBlank() && _uiState.value.cards.size >= MIN_CARDS_REQUIRES) {
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
            frontCreateCardState.clear()
            backCreateCardState.clear()
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
        editFrontState.setText(card.front)
        editBackState.setText(card.back)

        _uiState.update {
            it.copy(
                dialogState = DeckFormDialogState.EditFlashcard(
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
                dialogState = DeckFormDialogState.DeleteFlashcard(
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
}