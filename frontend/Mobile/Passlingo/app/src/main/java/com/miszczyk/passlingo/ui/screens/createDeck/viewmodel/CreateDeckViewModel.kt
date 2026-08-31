package com.miszczyk.passlingo.ui.screens.createDeck.viewmodel

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckDialogState
import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class CreateDeckViewModel(application: Application) : AndroidViewModel(application){
    private val _uiState = MutableStateFlow(CreateDeckUiState())
    val uiState: StateFlow<CreateDeckUiState> = _uiState.asStateFlow()
    val deckNameState = TextFieldState("")

    private val _navigateBack = Channel<Unit>(Channel.BUFFERED)
    val navigateBack = _navigateBack.receiveAsFlow()

    private val dialogAction = CreateDeckDialogAction(_uiState, viewModelScope, _navigateBack)


    fun onDialogCancelled() = dialogAction.onDialogCancelled()
    fun onDialogConfirmed() = dialogAction.onDialogConfirmed()

    fun onSaveDeckClicked(addedCards: Int){
        if(deckNameState.text.toString().isNotBlank() && addedCards > 0){
            _uiState.update { it.copy(dialogState = CreateDeckDialogState.SaveDeck) }
        }
    }

    fun onBackClicked(){
        _uiState.update { it.copy(dialogState = CreateDeckDialogState.DiscardChanges) }
    }
        fun onSelectIconClicked(){
        _uiState.update { it.copy(showBottomSheet = true) }
    }
}