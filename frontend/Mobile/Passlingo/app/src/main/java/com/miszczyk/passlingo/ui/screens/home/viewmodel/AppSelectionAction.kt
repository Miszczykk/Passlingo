package com.miszczyk.passlingo.ui.screens.home.viewmodel

import com.miszczyk.passlingo.ui.screens.home.datastore.COST_TIME
import com.miszczyk.passlingo.ui.screens.home.model.DecksUiState
import com.miszczyk.passlingo.ui.screens.home.model.DialogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AppSelectionAction(
    private val uiStateFlow: MutableStateFlow<DecksUiState>,
) {
    private fun toggleAppSelection(state: DecksUiState, packageName: String): Set<String> {
        return if (state.selectedApps.contains(packageName)) {
            state.selectedApps - packageName
        } else {
            state.selectedApps + packageName
        }
    }

    private fun resolveUnlockDialogState(state: DecksUiState, packageName: String): DialogState {
        return if (state.balanceTime < COST_TIME) {
            DialogState.InsufficientTime
        } else {
            DialogState.ConfirmUnlock(packageName)
        }
    }

    fun onAppToggled(packageName: String) {
        uiStateFlow.update { state ->
            if (!state.lockedApps.contains(packageName)) {
                state.copy(selectedApps = toggleAppSelection(state, packageName))
            } else {
                state.copy(dialogState = resolveUnlockDialogState(state, packageName))
            }
        }
    }

    fun onBlockSelectedClicked() {
        if (uiStateFlow.value.selectedApps.isNotEmpty()) {
            uiStateFlow.update { state ->
                state.copy(dialogState = DialogState.ConfirmLock)
            }
        }
    }

    fun onSelectionCleared() {
        uiStateFlow.update { state ->
            state.copy(selectedApps = emptySet())
        }
    }
}