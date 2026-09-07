package com.miszczyk.passlingo.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.miszczyk.passlingo.ui.theme.Color.mono200
import com.miszczyk.passlingo.ui.theme.Color.mono400
import com.miszczyk.passlingo.ui.theme.Color.mono50
import com.miszczyk.passlingo.ui.theme.Color.mono500
import com.miszczyk.passlingo.ui.theme.Color.navy
import com.miszczyk.passlingo.ui.theme.Color.red
import com.miszczyk.passlingo.ui.theme.Color.yellow

private val LightColorScheme = lightColorScheme(
    background = mono50,
    primary = navy,
    secondary = yellow,
    onSecondary = mono500,
    onBackground = mono200,
    onSurface = mono400,
    error = red
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