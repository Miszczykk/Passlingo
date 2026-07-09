package com.miszczyk.passlingo.ui.screens.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.ui.screens.home.data.AppUsageProvider
import com.miszczyk.passlingo.ui.screens.home.data.BlockedAppsRepository
import com.miszczyk.passlingo.ui.screens.home.model.AppItem
import com.miszczyk.passlingo.ui.screens.home.util.hasUsageStatsPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DecksUiState(
    val showBottomSheet: Boolean = false,
    val showAlertDialog: Boolean = false,
    val userApps: List<AppItem> = emptyList(),
    val selectedApps: Set<String> = emptySet(),
    val blockedApps: Set<String> = emptySet(),
    val hasUsagePermission: Boolean = true,
    val isLoadingApps: Boolean = false
)

class HomeViewModel(application: Application) : AndroidViewModel(application){
    private val appUsageProvider = AppUsageProvider(application)
    private val blockedAppsRepository = BlockedAppsRepository(application)

    private val _uiState = MutableStateFlow(DecksUiState())
    val uiState: StateFlow<DecksUiState> = _uiState.asStateFlow()

    init {
        observeBlockedApps()
    }

    private fun observeBlockedApps(){
        viewModelScope.launch {
            blockedAppsRepository.blockedApps.collect { blocked ->
                _uiState.update { it.copy(blockedApps = blocked) }
            }
        }
    }

    fun onLockIconClicked(){
        _uiState.update { it.copy(showBottomSheet = true) }
        checkPermissionAndLoadApps()
    }

    private fun checkPermissionAndLoadApps(){
        val hasPermission = hasUsageStatsPermission(getApplication())
        _uiState.update { it.copy(hasUsagePermission = hasPermission) }

        if(hasPermission){
            loadInstalledApps()
        }
    }

    private fun loadInstalledApps(){
        _uiState.update { it.copy(isLoadingApps = true) }
        viewModelScope.launch {
            val apps = withContext(Dispatchers.IO){
                appUsageProvider.getInstalledAppsWithUsage()
            }
            _uiState.update { it.copy(userApps = apps, isLoadingApps = false) }
        }
    }

    fun onAppToggled(packageName: String){
        _uiState.update { state ->
            val newSelection = if(state.selectedApps.contains(packageName)){
                state.selectedApps - packageName
            }else{
                state.selectedApps + packageName
            }
            state.copy(selectedApps = newSelection)
        }
    }

    fun onBlockSelectedClicked(){
        if(_uiState.value.selectedApps.isNotEmpty()){
            _uiState.update { it.copy(showAlertDialog = true) }
        }
    }

    fun onSheetDismissed(){
        _uiState.update { it.copy(showBottomSheet = false, selectedApps = emptySet()) }
    }

    fun onDialogCancelled(){
        _uiState.update { it.copy(showAlertDialog = false,
            showBottomSheet = false,
            selectedApps = emptySet())}
    }

    fun onDialogConfirmed(){
        val selection = _uiState.value.selectedApps
        viewModelScope.launch {
            blockedAppsRepository.addBlockedApps(selection)
        }
        _uiState.update {
            it.copy(
                showAlertDialog = false,
                showBottomSheet = false,
                selectedApps = emptySet()
            )
        }
    }

    fun onReturnedFromSettings(){
        if(_uiState.value.showBottomSheet){
            checkPermissionAndLoadApps()
        }
    }
}

