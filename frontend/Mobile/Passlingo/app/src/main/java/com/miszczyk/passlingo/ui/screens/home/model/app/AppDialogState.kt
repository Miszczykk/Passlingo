package com.miszczyk.passlingo.ui.screens.home.model.app

sealed interface HasPackageName {
    val packageName: String
}
sealed interface AppDialogState {
    data object None : AppDialogState
    data object ConfirmLock : AppDialogState
    data class ConfirmUnlock(override val packageName: String) : AppDialogState, HasPackageName
    data class InsufficientTime(override val packageName: String) : AppDialogState, HasPackageName
    data class Error(val message: String) : AppDialogState
}