package com.miszczyk.passlingo.ui.screens.createDeck.viewmodel

import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckDialogState
import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateDeckDialogAction(
    private val uiStateFlow: MutableStateFlow<CreateDeckUiState>,
    private val externalScope: CoroutineScope,
    private val navigateBack :  Channel<Unit>
){
    fun onDialogCancelled() {
        uiStateFlow.update { state -> state.copy(dialogState = CreateDeckDialogState.None) }
    }

    private fun onDiscardDialogConfirmed(){
        uiStateFlow.update { state -> state.copy(dialogState = CreateDeckDialogState.None) }
        externalScope.launch {
            navigateBack.send(Unit)
        }
    }

    private fun onSaveDeckDialogConfirmed(){
        //TODO SAVE DECK
        uiStateFlow.update { state -> state.copy(dialogState = CreateDeckDialogState.None) }

        externalScope.launch {
            navigateBack.send(Unit)
        }
    }

    fun onDialogConfirmed(){
        when(val currentState = uiStateFlow.value.dialogState){
            is CreateDeckDialogState.None -> error("onDialogConfirmed called with no dialog visible")
            is CreateDeckDialogState.SaveDeck -> onSaveDeckDialogConfirmed()
            is CreateDeckDialogState.DiscardChanges -> onDiscardDialogConfirmed()
            is CreateDeckDialogState.Error -> onDialogCancelled()
        }
    }
}