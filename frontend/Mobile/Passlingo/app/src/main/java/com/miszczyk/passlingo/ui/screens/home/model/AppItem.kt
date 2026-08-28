package com.miszczyk.passlingo.ui.screens.home.model

import androidx.compose.ui.graphics.ImageBitmap

data class AppItem(
    val name: String,
    val packageName: String,
    val icon: ImageBitmap,
    val timeInForeground: Long
)