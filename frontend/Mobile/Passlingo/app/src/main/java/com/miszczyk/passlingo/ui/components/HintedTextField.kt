package com.miszczyk.passlingo.ui.components

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontFamily
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium

@Composable
fun HintedTextField(state: TextFieldState, hintText: String, fontFamily: FontFamily) {
    Text(
        text = hintText,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = titleMedium,
        modifier = Modifier.alpha(alpha = if (state.text.isEmpty()) 1f else 0f),
        fontFamily = fontFamily
    )
}