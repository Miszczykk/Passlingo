package com.miszczyk.passlingo.ui.screens.home.viewmodel

import com.miszczyk.passlingo.ui.screens.home.data.Repository
import com.miszczyk.passlingo.ui.screens.home.model.DeckUiState
import com.miszczyk.passlingo.ui.screens.home.model.DialogState
import com.miszczyk.passlingo.ui.screens.home.util.Constants.COST_TIME
import com.miszczyk.passlingo.ui.screens.home.util.earnedTimeFor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DialogAction(
    private val uiStateFlow: MutableStateFlow<DeckUiState>,
    private val externalScope: CoroutineScope,
    private val lockedAppsAndEarnedTimeRepository: Repository,
) {
    fun onDialogCancelled() {
        uiStateFlow.update { state -> state.copy(dialogState = DialogState.None) }
    }

    private fun onLockAppDialogConfirmed() {
        val selection = uiStateFlow.value.selectedApps
        executeDialogTask(
            task = {
                lockedAppsAndEarnedTimeRepository.lockAppsAndAddCreditTime(selection, earnedTimeFor(selection.size))
            },
            onSuccessStateUpdate = { state ->
                state.copy(dialogState = DialogState.None, selectedApps = emptySet())
            }
        )
    }

    private fun onUnlockAppDialogConfirmed(packageName: String) {
        executeDialogTask(
            task = {
                lockedAppsAndEarnedTimeRepository.unlockAppAndSubtractCreditTime(packageName, COST_TIME)
            },
            onSuccessStateUpdate = { state ->
                state.copy(dialogState = DialogState.None)
            }
        )
    }

    private fun onInsufficientTimeDialogConfirmed() {
        uiStateFlow.update { state ->
            state.copy(
                dialogState = DialogState.None,
                showBottomSheet = false
            )
        }
    }

    fun onDialogConfirmed() {
        when (val currentState = uiStateFlow.value.dialogState) {
            is DialogState.None -> error("onDialogConfirmed called with no dialog visible")
            is DialogState.ConfirmLock -> onLockAppDialogConfirmed()
            is DialogState.ConfirmUnlock -> onUnlockAppDialogConfirmed(currentState.packageName)
            is DialogState.InsufficientTime -> onInsufficientTimeDialogConfirmed()
            is DialogState.Error -> onDialogCancelled()
        }
    }

    private fun executeDialogTask(
        task: suspend () -> Unit,
        onSuccessStateUpdate: (DeckUiState) -> DeckUiState
    ) {
        externalScope.launch {
            runCatching {
                task()
            }.onSuccess {
                uiStateFlow.update { state -> onSuccessStateUpdate(state) }
            }.onFailure { exception ->
                if (exception is CancellationException) throw exception
                uiStateFlow.update { state ->
                    state.copy(dialogState = DialogState.Error(exception.localizedMessage ?: "Unknown error occurred"))
                }
            }
        }
    }
}