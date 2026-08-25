package com.miszczyk.passlingo.ui.screens.home.viewmodel

import com.miszczyk.passlingo.ui.screens.home.data.LockedAppsRepository
import com.miszczyk.passlingo.ui.screens.home.data.TimeRepository
import com.miszczyk.passlingo.ui.screens.home.datastore.COST_TIME
import com.miszczyk.passlingo.ui.screens.home.model.DecksUiState
import com.miszczyk.passlingo.ui.screens.home.model.DialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DialogAction(
    private val uiStateFlow: MutableStateFlow<DecksUiState>,
    private val viewModelScope: CoroutineScope,
    private val lockedAppsRepository: LockedAppsRepository,
    private val timeRepository: TimeRepository

) {
    fun onDialogCancelled() {
        uiStateFlow.update { state -> state.copy(dialogState = DialogState.None) }
    }

    private fun onLockAppDialogConfirmed() {
        val selection = uiStateFlow.value.selectedApps
        val timeEarned = (COST_TIME / 4) * selection.size

        viewModelScope.launch {
            lockedAppsRepository.addLockedApps(selection)
            timeRepository.addTime(timeEarned)
        }

        uiStateFlow.update { state ->
            state.copy(dialogState = DialogState.None, selectedApps = emptySet())
        }
    }

    private fun onUnlockAppDialogConfirmed(packageName: String) {
        viewModelScope.launch {
            lockedAppsRepository.removeSingleLockedApp(setOf(packageName))
            timeRepository.substractTime(COST_TIME)
        }

        uiStateFlow.update { state ->
            state.copy(dialogState = DialogState.None)
        }
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
        }
    }
}