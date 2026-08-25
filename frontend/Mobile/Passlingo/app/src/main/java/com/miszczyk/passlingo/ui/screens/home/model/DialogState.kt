package com.miszczyk.passlingo.ui.screens.home.model

sealed interface DialogState {
    data object None : DialogState
    data object ConfirmLock : DialogState
    data class ConfirmUnlock(val packageName: String) : DialogState
    data object InsufficientTime : DialogState
}