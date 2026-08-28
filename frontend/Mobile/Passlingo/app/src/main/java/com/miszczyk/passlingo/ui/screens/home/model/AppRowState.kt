package com.miszczyk.passlingo.ui.screens.home.model

sealed interface AppRowState {
    data object Normal : AppRowState
    data object Selected : AppRowState
    data object Locked : AppRowState
}