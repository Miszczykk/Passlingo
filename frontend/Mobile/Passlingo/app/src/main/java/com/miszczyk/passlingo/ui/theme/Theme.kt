package com.miszczyk.passlingo.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    background = mono50,
    primary = navy,
    secondary = yellow,
    onSecondary = mono500,
    onBackground = mono200
)

@Composable
fun PasslingoTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current

    if(!LocalView.current.isInEditMode){
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            insetsController.isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}