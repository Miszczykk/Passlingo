package com.miszczyk.passlingo.ui.screens.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.ui.screens.home.data.AppUsageProvider
import com.miszczyk.passlingo.ui.screens.home.data.LockedAppsRepository
import com.miszczyk.passlingo.ui.screens.home.data.TimeRepository
import com.miszczyk.passlingo.ui.screens.home.model.DecksUiState
import com.miszczyk.passlingo.ui.screens.home.util.hasUsageStatsPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DeckViewModel(application: Application) : AndroidViewModel(application) {
    private val appUsageProvider = AppUsageProvider(application)
    private val lockedAppsRepository = LockedAppsRepository(application)
    private val timeRepository = TimeRepository(application)
    private val _uiState = MutableStateFlow(DecksUiState())
    val uiState: StateFlow<DecksUiState> = _uiState.asStateFlow()
    private val dialogAction =
        DialogAction(_uiState, viewModelScope, lockedAppsRepository, timeRepository)
    private val appSelectionAction = AppSelectionAction(_uiState)


    init {
        combine(
            lockedAppsRepository.lockedApps,
            timeRepository.balanceTime
        ) { locked, time ->
            _uiState.update { it.copy(lockedApps = locked, balanceTime = time) }
        }.launchIn(viewModelScope)
    }

    private fun checkPermissionAndLoadApps() {
        val hasPermission = hasUsageStatsPermission(getApplication())
        val alreadyHadPermission = _uiState.value.hasUsagePermission

        _uiState.update { it.copy(hasUsagePermission = hasPermission) }

        if (hasPermission && alreadyHadPermission) {
            loadInstalledApps()
        }
    }

    private fun loadInstalledApps() {
        _uiState.update { it.copy(isLoadingApps = true) }
        viewModelScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    appUsageProvider.getInstalledAppsWithUsage()
                }
            }
            result.fold(
                onSuccess = { apps ->
                    _uiState.update { it.copy(userApps = apps, isLoadingApps = false) }
                },
                onFailure = { _uiState.update { it.copy(isLoadingApps = false) } }
            )
        }
    }


    fun onDialogCancelled() = dialogAction.onDialogCancelled()
    fun onDialogConfirmed() = dialogAction.onDialogConfirmed()
    fun onAppToggled(packageName: String) = appSelectionAction.onAppToggled(packageName)
    fun onBlockSelectedClicked() = appSelectionAction.onBlockSelectedClicked()

    fun onLockIconClicked() {
        _uiState.update { it.copy(showBottomSheet = true) }
        checkPermissionAndLoadApps()
    }

    fun onSheetDismissed() {
        _uiState.update { it.copy(showBottomSheet = false) }
        appSelectionAction.onSelectionCleared()
    }

    fun onReturnedFromSettings() {
        if (_uiState.value.showBottomSheet) {
            checkPermissionAndLoadApps()
        }
    }
}
