package com.miszczyk.passlingo.ui.screens.home.model

sealed interface HasPackageName {
    val packageName: String
}

sealed interface DialogState {
    data object None : DialogState
    data object ConfirmLock : DialogState
    data class ConfirmUnlock(override val packageName: String) : DialogState, HasPackageName
    data class InsufficientTime(override val packageName: String) : DialogState, HasPackageName
    data class Error(val message: String) : DialogState
}