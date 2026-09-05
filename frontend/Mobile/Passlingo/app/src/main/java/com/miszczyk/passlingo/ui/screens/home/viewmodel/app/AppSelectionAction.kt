package com.miszczyk.passlingo.ui.screens.home.viewmodel.app

import com.miszczyk.passlingo.ui.screens.home.model.app.AppUiState
import com.miszczyk.passlingo.ui.screens.home.model.app.AppDialogState
import com.miszczyk.passlingo.ui.screens.home.util.Constants.COST_TIME_SECONDS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AppSelectionAction(
    private val uiStateFlow: MutableStateFlow<AppUiState>,
) {
    private fun toggleAppSelection(state: AppUiState, packageName: String): Set<String> {
        return if (state.selectedApps.contains(packageName)) {
            state.selectedApps - packageName
        } else {
            state.selectedApps + packageName
        }
    }

    private fun resolveUnlockDialogState(state: AppUiState, packageName: String): AppDialogState {
        return if (state.balanceTime < COST_TIME_SECONDS) {
            AppDialogState.InsufficientTime(packageName)
        } else {
            AppDialogState.ConfirmUnlock(packageName)
        }
    }

    fun onAppToggled(packageName: String) {
        uiStateFlow.update { state ->
            if (!state.lockedApps.contains(packageName)) {
                state.copy(selectedApps = toggleAppSelection(state, packageName))
            } else {
                state.copy(appDialogState = resolveUnlockDialogState(state, packageName))
            }
        }
    }

    fun onLockSelectedClicked() {
        uiStateFlow.update { state ->
            if (state.selectedApps.isNotEmpty()) {
                state.copy(appDialogState = AppDialogState.ConfirmLock)
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