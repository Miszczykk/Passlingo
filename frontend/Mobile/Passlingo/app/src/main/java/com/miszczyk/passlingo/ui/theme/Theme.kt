package com.miszczyk.passlingo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}