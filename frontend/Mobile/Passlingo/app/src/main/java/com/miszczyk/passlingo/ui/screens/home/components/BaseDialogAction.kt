package com.miszczyk.passlingo.ui.screens.home.components

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseDialogAction<State, DialogState>(
    protected val uiStateFlow: MutableStateFlow<State>,
    protected val externalScope: CoroutineScope
) {
    protected abstract val noneDialogState: DialogState
    protected abstract fun createErrorDialogState(message: String): DialogState
    protected abstract fun updateStateWithDialog(state: State, dialogState: DialogState): State

    abstract fun onDialogConfirmed()

    fun onDialogCancelled(){
        uiStateFlow.update { state -> updateStateWithDialog(state, noneDialogState)}
    }

    protected fun executeDialogTask(
        task: suspend () -> Unit, onSuccessStateUpdate: (State) -> State
    ){
        externalScope.launch {
            runCatching {
                task()
            }.onSuccess {
                uiStateFlow.update { state -> onSuccessStateUpdate(state) }
            }.onFailure { exception ->
                if(exception is CancellationException) throw exception
                uiStateFlow.update { state ->
                    updateStateWithDialog(state, createErrorDialogState(exception.localizedMessage ?: "Unknown error occurred"))
                }
            }
        }
    }
}