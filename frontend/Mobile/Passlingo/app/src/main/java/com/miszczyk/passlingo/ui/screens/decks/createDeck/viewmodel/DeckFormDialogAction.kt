package com.miszczyk.passlingo.ui.screens.decks.createDeck.viewmodel

import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.DeckFormDialogState
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.DeckFormUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckFormDialogAction(
    private val uiStateFlow: MutableStateFlow<DeckFormUiState>,
    private val externalScope: CoroutineScope,
    private val navigateBack: Channel<Unit>,
    private val saveDeck: suspend () -> Unit,
    private val clearScreen: () -> Unit,
    private val onEditCardConfirmed: (String) -> Unit,
    private val onDeleteConfirmed: (String) -> Unit,
) {
    fun onDialogCancelled() {
        uiStateFlow.update { state -> state.copy(dialogState = DeckFormDialogState.None) }

    }

    fun onDiscardDialogConfirmed() {
        clearScreen()
        uiStateFlow.update { state -> state.copy(dialogState = DeckFormDialogState.None) }
        externalScope.launch {
            navigateBack.send(element = Unit)
        }
    }

    private fun onSaveDeckDialogConfirmed() {
        externalScope.launch {
            runCatching {
                saveDeck()
            }.onSuccess {
                clearScreen()
                uiStateFlow.update { it.copy(dialogState = DeckFormDialogState.None) }
                navigateBack.send(element = Unit)
            }.onFailure { exception ->
                uiStateFlow.update {
                    it.copy(
                        dialogState = DeckFormDialogState.Error(
                            message = exception.localizedMessage ?: "Failed to save deck"
                        )
                    )
                }
            }
        }
    }

    private fun onDeleteCardDialogConfirmed(id: String) {
        onDeleteConfirmed(id)
        uiStateFlow.update { state -> state.copy(dialogState = DeckFormDialogState.None) }
    }

    private fun onEditCardDialogConfirmed(id: String) {
        onEditCardConfirmed(id)
        uiStateFlow.update { state -> state.copy(dialogState = DeckFormDialogState.None) }
    }

    fun onDialogConfirmed() {
        when (val currentState = uiStateFlow.value.dialogState) {
            is DeckFormDialogState.None -> error("onDialogConfirmed called with no dialog visible")
            is DeckFormDialogState.SaveDeckForm -> onSaveDeckDialogConfirmed()
            is DeckFormDialogState.DiscardChanges -> onDiscardDialogConfirmed()
            is DeckFormDialogState.Error -> onDialogCancelled()
            is DeckFormDialogState.DeleteFlashcard -> onDeleteCardDialogConfirmed(currentState.id)
            is DeckFormDialogState.EditFlashcard -> onEditCardDialogConfirmed(currentState.id)
        }
    }
}