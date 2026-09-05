package com.miszczyk.passlingo.ui.screens.home.viewmodel.app

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.ui.screens.home.data.AppUsageProvider
import com.miszczyk.passlingo.ui.screens.home.data.RepositoryTimeAndApps
import com.miszczyk.passlingo.ui.screens.home.model.app.AppUiState
import com.miszczyk.passlingo.ui.screens.home.model.app.AppDialogState
import com.miszczyk.passlingo.ui.screens.home.util.hasUsageStatsPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val appUsageProvider = AppUsageProvider(context = application)
    private val repositoryTimeAndApps = RepositoryTimeAndApps(context = application)
    private val _appUiState = MutableStateFlow(value = AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState.asStateFlow()
    private val appDialogAction = AppDialogAction(uiStateFlow = _appUiState, externalScope = viewModelScope, lockedAppsAndEarnedTimeRepositoryTimeAndApps = repositoryTimeAndApps)
    private val appSelectionAction = AppSelectionAction(uiStateFlow = _appUiState)

    private var observationJob: Job? = null

    init {
        startObservingData()
    }

    private fun startObservingData() {
        observationJob?.cancel()

        observationJob = combine(
            flow = repositoryTimeAndApps.lockedApps, flow2 =  repositoryTimeAndApps.balanceTime
        ) { locked, time ->
            locked to time
        }.onEach { (locked, time) ->
            _appUiState.update { it.copy(lockedApps = locked, balanceTime = time) }
        }.retry(retries = 3) { _ ->
            delay(timeMillis = 1000)
            true
        }.catch { e ->
            val errorMessage = e.localizedMessage ?: "Failed to load data"
            Log.e("error", errorMessage)
            _appUiState.update { it.copy(appDialogState = AppDialogState.Error(errorMessage)) }
        }.launchIn(viewModelScope)
    }

    private fun loadInstalledApps() {
        _appUiState.update { it.copy(isLoadingApps = true) }
        viewModelScope.launch {
            val result = runCatching {
                withContext(context = Dispatchers.IO) {
                    appUsageProvider.getInstalledAppsWithUsage()
                }
            }
            result.fold(onSuccess = { apps ->
                _appUiState.update { it.copy(userApps = apps, isLoadingApps = false) }
            }, onFailure = { error ->
                Log.e("error", error.localizedMessage ?: "Failed to load apps")
                _appUiState.update {
                    it.copy(
                        isLoadingApps = false, appDialogState = AppDialogState.Error(
                            message = error.localizedMessage ?: "Failed to load apps"
                        )
                    )

                }
            })
        }
    }

    fun onDialogCancelled() = appDialogAction.onDialogCancelled()
    fun onDialogConfirmed() = appDialogAction.onDialogConfirmed()
    fun onAppToggled(packageName: String) = appSelectionAction.onAppToggled(packageName)
    fun onLockSelectedClicked() = appSelectionAction.onLockSelectedClicked()

    private fun checkPermissionAndLoadApps() {
        val hasPermission = hasUsageStatsPermission(getApplication())

        _appUiState.update { it.copy(hasUsagePermission = hasPermission) }

        if (hasPermission) {
            loadInstalledApps()
        }
    }

    fun onLockIconClicked() {
        _appUiState.update { it.copy(showBottomSheet = true) }
        checkPermissionAndLoadApps()
    }

    fun onSheetDismissed() {
        _appUiState.update { it.copy(showBottomSheet = false) }
        appSelectionAction.onSelectionCleared()
        onDialogCancelled()
    }

    fun onReturnedFromSettings() {
        if (_appUiState.value.showBottomSheet) {
            checkPermissionAndLoadApps()
        }
    }

    fun onRetryErrorClicked() {
        _appUiState.update { it.copy(appDialogState = AppDialogState.None) }
        startObservingData()
    }

    fun showPermissionError(errorMessage: String) {
        _appUiState.update { it.copy(appDialogState = AppDialogState.Error(errorMessage)) }
    }
}
