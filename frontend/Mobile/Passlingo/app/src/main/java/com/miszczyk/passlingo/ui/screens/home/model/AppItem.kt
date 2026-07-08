package com.miszczyk.passlingo.ui.screens.home.model

import android.graphics.drawable.Drawable

data class AppItem(
    val name: String,
    val packageName: String,
    val icon: Drawable,
    val timeInForeground: Long
)