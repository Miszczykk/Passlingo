package com.miszczyk.passlingo.ui.screens.createDeck.viewmodel

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckDialogState
import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class CreateDeckViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(CreateDeckUiState())
    val uiState: StateFlow<CreateDeckUiState> = _uiState.asStateFlow()

    private val _navigateBack = Channel<Unit>(Channel.BUFFERED)
    val navigateBack = _navigateBack.receiveAsFlow()

    private val dialogAction = CreateDeckDialogAction(_uiState, viewModelScope, _navigateBack)


    fun onDialogCancelled() = dialogAction.onDialogCancelled()
    fun onDialogConfirmed() = dialogAction.onDialogConfirmed()
    fun onBack() = dialogAction.onDiscardDialogConfirmed()

    fun onSaveDeckClicked(deckName: String, addedCards: Int) {
        if (deckName.isNotBlank() && addedCards > 0) {
            _uiState.update { it.copy(dialogState = CreateDeckDialogState.SaveDeck) }
        }
    }

    fun onBackClicked(deckName: String, addedCards: Int) {
        if(deckName.isNotBlank() || addedCards > 0){
            _uiState.update { it.copy(dialogState = CreateDeckDialogState.DiscardChanges) }
        }else{
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

    fun onAddToDeckClicked(addedCards: Int, frontCardState: TextFieldState, backCardState: TextFieldState) {
        if (frontCardState.text.toString().isNotBlank() && backCardState.text.toString()
                .isNotBlank()
        ) {
            _uiState.update { it.copy(addedCards = addedCards + 1) }

            frontCardState.edit { replace(0, length, "") }
            backCardState.edit { replace(0, length, "") }
        } else {
            _uiState.update { it.copy(dialogState = CreateDeckDialogState.Error(getApplication<Application>().getString(R.string.dialog_message_incomplete_flashcards))) }
        }
    }

    fun onSheetDismissed() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }
}