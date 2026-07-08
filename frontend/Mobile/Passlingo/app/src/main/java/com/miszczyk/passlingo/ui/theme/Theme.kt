package com.miszczyk.passlingo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    background = mono_50,
    primary = navy,
    secondary = yellow,
    onSecondary = mono_500,
    onBackground = mono_200
)

@Composable
fun PasslingoTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}