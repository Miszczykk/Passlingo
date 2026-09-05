package com.miszczyk.passlingo.ui.screens.home.model.app

data class AppUiState(
    val showBottomSheet: Boolean = false,
    val appDialogState: AppDialogState = AppDialogState.None,
    val userApps: List<AppItem> = emptyList(),
    val selectedApps: Set<String> = emptySet(),
    val lockedApps: Set<String> = emptySet(),

    val hasUsagePermission: Boolean = false,
    val isLoadingApps: Boolean = false,
    val balanceTime: Long = 0L,
)
