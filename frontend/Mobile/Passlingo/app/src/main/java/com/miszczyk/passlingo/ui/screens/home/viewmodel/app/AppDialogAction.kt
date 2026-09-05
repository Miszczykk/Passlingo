package com.miszczyk.passlingo.ui.screens.home.viewmodel.app

import com.miszczyk.passlingo.ui.screens.home.data.RepositoryTimeAndApps
import com.miszczyk.passlingo.ui.screens.home.model.app.AppUiState
import com.miszczyk.passlingo.ui.screens.home.model.app.AppDialogState
import com.miszczyk.passlingo.ui.screens.home.util.Constants.COST_TIME_SECONDS
import com.miszczyk.passlingo.ui.util.earnedTimeFor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppDialogAction(
    private val uiStateFlow: MutableStateFlow<AppUiState>,
    private val externalScope: CoroutineScope,
    private val lockedAppsAndEarnedTimeRepositoryTimeAndApps: RepositoryTimeAndApps,
) {
    fun onDialogCancelled() {
        uiStateFlow.update { state -> state.copy(appDialogState = AppDialogState.None) }
    }

    private fun onLockAppDialogConfirmed() {
        val selection = uiStateFlow.value.selectedApps
        executeDialogTask(task = {
            lockedAppsAndEarnedTimeRepositoryTimeAndApps.lockAppsAndAddCreditTime(
                packageNames = selection,
                secondsEarned = earnedTimeFor(numberOfApplication = selection.size)
            )
        }, onSuccessStateUpdate = { state ->
            state.copy(appDialogState = AppDialogState.None, selectedApps = emptySet())
        })
    }

    private fun onUnlockAppDialogConfirmed(packageName: String) {
        executeDialogTask(task = {
            lockedAppsAndEarnedTimeRepositoryTimeAndApps.unlockAppAndSubtractCreditTime(
                packageName, secondsLost = COST_TIME_SECONDS
            )
        }, onSuccessStateUpdate = { state ->
            state.copy(appDialogState = AppDialogState.None)
        })
    }

    private fun onInsufficientTimeDialogConfirmed() {
        uiStateFlow.update { state ->
            state.copy(
                appDialogState = AppDialogState.None, showBottomSheet = false
            )
        }
    }

    fun onDialogConfirmed() {
        when (val currentState = uiStateFlow.value.appDialogState) {
            is AppDialogState.None -> error("onDialogConfirmed called with no dialog visible")
            is AppDialogState.ConfirmLock -> onLockAppDialogConfirmed()
            is AppDialogState.ConfirmUnlock -> onUnlockAppDialogConfirmed(currentState.packageName)
            is AppDialogState.InsufficientTime -> onInsufficientTimeDialogConfirmed()
            is AppDialogState.Error -> onDialogCancelled()
        }
    }

    private fun executeDialogTask(
        task: suspend () -> Unit, onSuccessStateUpdate: (AppUiState) -> AppUiState
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
                        appDialogState = AppDialogState.Error(
                            exception.localizedMessage ?: "Unknown error occurred"
                        )
                    )
                }
            }
        }
    }
}