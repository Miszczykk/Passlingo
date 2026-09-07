package com.miszczyk.passlingo.ui.screens.home.viewmodel.app

import com.miszczyk.passlingo.ui.screens.home.components.BaseDialogAction
import com.miszczyk.passlingo.ui.screens.home.data.TimeAndAppsRepository
import com.miszczyk.passlingo.ui.screens.home.model.app.AppDialogState
import com.miszczyk.passlingo.ui.screens.home.model.app.AppUiState
import com.miszczyk.passlingo.ui.screens.home.util.Constants.COST_TIME_SECONDS
import com.miszczyk.passlingo.ui.util.earnedTimeFor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AppDialogAction(
    uiStateFlow: MutableStateFlow<AppUiState>,
    externalScope: CoroutineScope,
    private val repository: TimeAndAppsRepository,
) : BaseDialogAction<AppUiState, AppDialogState>(uiStateFlow, externalScope) {

    override val noneDialogState: AppDialogState = AppDialogState.None
    override fun createErrorDialogState(message: String): AppDialogState =
        AppDialogState.Error(message)

    override fun updateStateWithDialog(state: AppUiState, dialogState: AppDialogState): AppUiState =
        state.copy(appDialogState = dialogState)

    override fun onDialogConfirmed() {
        when (val currentState = uiStateFlow.value.appDialogState) {
            is AppDialogState.None -> error("onDialogConfirmed called with no dialog visible")
            is AppDialogState.ConfirmLock -> onLockAppDialogConfirmed()
            is AppDialogState.ConfirmUnlock -> onUnlockAppDialogConfirmed(currentState.packageName)
            is AppDialogState.InsufficientTime -> onInsufficientTimeDialogConfirmed()
            is AppDialogState.Error -> onDialogCancelled()
        }
    }

    private fun onLockAppDialogConfirmed() {
        val selection = uiStateFlow.value.selectedApps
        executeDialogTask(task = {
            repository.lockAppsAndAddCreditTime(
                packageNames = selection,
                secondsEarned = earnedTimeFor(numberOfApplications = selection.size)
            )
        }, onSuccessStateUpdate = { state ->
            state.copy(appDialogState = AppDialogState.None, selectedApps = emptySet())
        })
    }

    private fun onUnlockAppDialogConfirmed(packageName: String) {
        executeDialogTask(task = {
            repository.unlockAppAndSubtractCreditTime(
                packageName, secondsLost = COST_TIME_SECONDS
            )
        }, onSuccessStateUpdate = { state ->
            state.copy(appDialogState = AppDialogState.None)
        })
    }

    private fun onInsufficientTimeDialogConfirmed() {
        uiStateFlow.update { state ->
            state.copy(appDialogState = AppDialogState.None, showBottomSheet = false)
        }
    }
}