package com.miszczyk.passlingo.ui.screens.home.viewmodel.deck

import com.miszczyk.passlingo.data.repository.DeckRepository
import com.miszczyk.passlingo.data.repository.StudySessionRepositoryImpl
import com.miszczyk.passlingo.ui.screens.home.components.BaseDialogAction
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckBottomSheetState
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckDialogState
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DeckDialogAction(
    uiStateFlow: MutableStateFlow<DeckUiState>,
    externalScope: CoroutineScope,
    private val deckRepository: DeckRepository,
    private val sessionRepository: StudySessionRepositoryImpl
) : BaseDialogAction<DeckUiState, DeckDialogState>(uiStateFlow, externalScope) {

    override val noneDialogState: DeckDialogState = DeckDialogState.None
    override fun createErrorDialogState(message: String): DeckDialogState =
        DeckDialogState.Error(message)

    override fun updateStateWithDialog(state: DeckUiState, dialogState: DeckDialogState): DeckUiState =
        state.copy(deckDialogState = dialogState)

    override fun onDialogConfirmed(){
        when(val currentState = uiStateFlow.value.deckDialogState){
            is DeckDialogState.None -> error("onDialogConfirmed called with no dialog visible")
            is DeckDialogState.ConfirmDelete -> onDeleteDeckConfirmed()
            is DeckDialogState.Error -> onDialogCancelled()
            is DeckDialogState.ResumeSession -> onContinueSessionConfirmed()
        }
    }

    private fun onDeleteDeckConfirmed(){
        val deckId = uiStateFlow.value.selectedDeckId ?: return
        executeDialogTask(
            task = { deckRepository.deleteDeck(deckId) },
            onSuccessStateUpdate = { state ->
                state.copy(
                    deckDialogState = DeckDialogState.None,
                    deckBottomSheetState = DeckBottomSheetState.None,
                    selectedDeckId = null
                )
            }
        )
    }
    fun onContinueSessionCancelled(){
        val deckId = uiStateFlow.value.selectedDeckId ?: return
        executeDialogTask(
            task = {
                sessionRepository.deleteSessionByDeckId(deckId)
            }, onSuccessStateUpdate = {state ->
                state.copy(
                    deckDialogState = DeckDialogState.None,
                    deckBottomSheetState = DeckBottomSheetState.StudySettings
                )
            }
        )
    }

    private fun onContinueSessionConfirmed(){
        uiStateFlow.update {
            it.copy(
                deckDialogState = DeckDialogState.None,
                deckBottomSheetState = DeckBottomSheetState.None
            )
        }
    }
}