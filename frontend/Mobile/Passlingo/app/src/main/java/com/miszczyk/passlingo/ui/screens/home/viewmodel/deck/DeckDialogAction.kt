package com.miszczyk.passlingo.ui.screens.home.viewmodel.deck

import com.miszczyk.passlingo.data.repository.DeckRepository
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckDialogState
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class DeckDialogAction(
    private val uiStateFlow: MutableStateFlow<DeckUiState>,
    private val externalScope: CoroutineScope,
    private val deckRepository: DeckRepository,
) {
    fun onDialogCancelled(){
        uiStateFlow.update { state -> state.copy(deckDialogState = DeckDialogState.None) }
    }

    private fun onDeleteDeckConfirmed(){
        val deckId = uiStateFlow.value.selectedDeckId ?: return

        executeDialogTask(
            task = { deckRepository.deleteDeck(deckId) },
            onSuccessStateUpdate = { state ->
                state.copy(
                    deckDialogState = DeckDialogState.None,
                    showBottomSheet = false,
                    selectedDeckId = null
                )
            }
        )
    }

    fun onDialogConfirmed(){
        when(val currentState = uiStateFlow.value.deckDialogState){
            is DeckDialogState.None -> error("onDialogConfirmed called with no dialog visible")
            is DeckDialogState.ConfirmDelete -> onDeleteDeckConfirmed()
            is DeckDialogState.Error -> onDialogCancelled()
        }
    }

    private fun executeDialogTask(
        task: suspend () -> Unit, onSuccessStateUpdate: (DeckUiState) -> DeckUiState
    ) {
        externalScope.launch {
            runCatching {
                task()
            }.onSuccess {
                uiStateFlow.update { state -> onSuccessStateUpdate(state) }
            }.onFailure { exception ->
                if (exception is CancellationException) throw exception
                uiStateFlow.update { state ->
                    state.copy(
                        deckDialogState = DeckDialogState.Error(
                            exception.localizedMessage ?: "Unknown error occurred"
                        )
                    )
                }
            }
        }
    }
}