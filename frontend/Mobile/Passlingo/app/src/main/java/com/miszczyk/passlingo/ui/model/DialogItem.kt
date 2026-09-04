package com.miszczyk.passlingo.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class DialogItem(
    val title: String,
    val message: String,
    val onConfirmText: String,
    val onCancelText: String? = null,
    val onConfirmTextColor: Color? = null,
    val onConfirmBackgroundColor: Color? = null,
    val extraContent: @Composable (() -> Unit)? = null,
    val isWide: Boolean = false
)