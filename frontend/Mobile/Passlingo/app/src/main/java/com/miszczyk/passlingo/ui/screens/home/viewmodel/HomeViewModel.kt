package com.miszczyk.passlingo.ui.screens.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.ui.screens.home.data.AppUsageProvider
import com.miszczyk.passlingo.ui.screens.home.data.BlockedAppsRepository
import com.miszczyk.passlingo.ui.screens.home.data.TimeRepository
import com.miszczyk.passlingo.ui.screens.home.datastore.COST_TIME
import com.miszczyk.passlingo.ui.screens.home.model.AppItem
import com.miszczyk.passlingo.ui.screens.home.util.hasUsageStatsPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DecksUiState(
    val showBottomSheet: Boolean = false,
    val showAlertDialog: Boolean = false,
    val showAlertUnblockDialog: Boolean = false,
    val showAlertWithoutTime: Boolean = false,
    val appToUnblock: String? = null,
    val userApps: List<AppItem> = emptyList(),
    val selectedApps: Set<String> = emptySet(),
    val blockedApps: Set<String> = emptySet(),
    val hasUsagePermission: Boolean = true,
    val isLoadingApps: Boolean = false,
    val balanceTime: Long = 0L
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val appUsageProvider = AppUsageProvider(application)
    private val blockedAppsRepository = BlockedAppsRepository(application)
    private val timeRepository = TimeRepository(application)

    private val _uiState = MutableStateFlow(DecksUiState())
    val uiState: StateFlow<DecksUiState> = _uiState.asStateFlow()

    init {
        observeBlockedApps()
        observeTime()
    }

    private fun observeBlockedApps() {
        viewModelScope.launch {
            blockedAppsRepository.blockedApps.collect { blocked ->
                _uiState.update { it.copy(blockedApps = blocked) }
            }
        }
    }

    private fun observeTime(){
        viewModelScope.launch {
            timeRepository.balanceTime.collect { time ->
                _uiState.update { it.copy(balanceTime = time) }
            }
        }
    }

    fun onLockIconClicked() {
        _uiState.update { it.copy(showBottomSheet = true) }
        checkPermissionAndLoadApps()
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
            val apps = withContext(Dispatchers.IO) {
                appUsageProvider.getInstalledAppsWithUsage()
            }
            _uiState.update { it.copy(userApps = apps, isLoadingApps = false) }
        }
    }

    fun onAppToggled(packageName: String) {
        _uiState.update { state ->
            if (!state.blockedApps.contains(packageName)) {
                val newSelection =  if (state.selectedApps.contains(packageName)) {
                    state.selectedApps - packageName
                } else {
                    state.selectedApps + packageName
                }
                state.copy(selectedApps = newSelection)
            }else{
                if(state.balanceTime < COST_TIME){
                    state.copy(
                        showAlertWithoutTime = true,
                    )
                }else{
                    state.copy(
                        showAlertUnblockDialog = true,
                        appToUnblock = packageName
                    )
                }

            }
        }
    }

    fun onBlockSelectedClicked() {
        if (_uiState.value.selectedApps.isNotEmpty()) {
            _uiState.update { it.copy(showAlertDialog = true) }
        }
    }

    fun onBlockedAppsClicked() {
        _uiState.update {
            it.copy(showAlertUnblockDialog = true)
        }
    }


    fun onSheetDismissed() {
        _uiState.update { it.copy(showBottomSheet = false, selectedApps = emptySet()) }
    }

    fun onDialogCancelled() {
        _uiState.update {
            it.copy(
                showAlertDialog = false
            )
        }
    }

    fun onDialogConfirmed() {
        val selection = _uiState.value.selectedApps
        val timeEarned = 900L * selection.size

        viewModelScope.launch {
            blockedAppsRepository.addBlockedApps(selection)
            timeRepository.addTime(timeEarned)
        }
        _uiState.update {
            it.copy(
                showAlertDialog = false,
                selectedApps = emptySet()
            )
        }
    }

    fun onUnblockDialogCancelled() {
        _uiState.update {
            it.copy(
                showAlertUnblockDialog = false,
                appToUnblock = null
            )
        }
    }

    fun onUnblockDialogConfirmed() {
        val appToUnblock = _uiState.value.appToUnblock

        if (appToUnblock != null) {
            viewModelScope.launch {
                blockedAppsRepository.removeSingleBlockedApp(setOf(appToUnblock))
                timeRepository.substractTime(COST_TIME)
            }
        }

        _uiState.update {
            it.copy(
                showAlertUnblockDialog = false,
                appToUnblock = null
            )
        }
    }

    fun onBlockWithoutTimeClicked(){
        _uiState.update {
            it.copy(
                showAlertWithoutTime = false,
                showBottomSheet = false
            )
        }
    }

    fun onReturnedFromSettings() {
        if (_uiState.value.showBottomSheet) {
            checkPermissionAndLoadApps()
        }
    }
}