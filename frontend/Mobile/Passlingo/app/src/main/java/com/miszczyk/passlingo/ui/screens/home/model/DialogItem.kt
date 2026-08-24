package com.miszczyk.passlingo.ui.screens.home.model

import androidx.compose.runtime.Composable

data class DialogItem(
    val title: String,
    val message: String,
    val onConfirm: () -> Unit,
    val onConfirmText: String,
    val onCancel: () -> Unit,
    val onCancelText: String? = null,
    val extraContent: @Composable (() -> Unit)? = null
)
