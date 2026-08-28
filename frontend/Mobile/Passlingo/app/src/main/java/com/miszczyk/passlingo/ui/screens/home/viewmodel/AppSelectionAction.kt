package com.miszczyk.passlingo.ui.screens.home.viewmodel

import com.miszczyk.passlingo.ui.screens.home.model.DeckUiState
import com.miszczyk.passlingo.ui.screens.home.model.DialogState
import com.miszczyk.passlingo.ui.screens.home.util.Constants.COST_TIME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AppSelectionAction(
    private val uiStateFlow: MutableStateFlow<DeckUiState>,
) {
    private fun toggleAppSelection(state: DeckUiState, packageName: String): Set<String> {
        return if (state.selectedApps.contains(packageName)) {
            state.selectedApps - packageName
        } else {
            state.selectedApps + packageName
        }
    }

    private fun resolveUnlockDialogState(state: DeckUiState, packageName: String): DialogState {
        return if (state.balanceTime < COST_TIME) {
            DialogState.InsufficientTime(packageName)
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

    fun onLockSelectedClicked() {
        uiStateFlow.update { state ->
            if (state.selectedApps.isNotEmpty()) {
                state.copy(dialogState = DialogState.ConfirmLock)
            } else {
                state
            }
        }
    }

    fun onSelectionCleared() {
        uiStateFlow.update { state ->
            state.copy(selectedApps = emptySet())
        }
    }
}