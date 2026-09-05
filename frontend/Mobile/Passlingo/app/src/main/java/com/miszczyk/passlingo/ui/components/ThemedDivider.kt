package com.miszczyk.passlingo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun ThemedDivider(
    colorLine: Color, strokeWidth: Float = 1f
) {
    Canvas(modifier = Modifier.fillMaxWidth()) {
        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = 0f),
            color = colorLine,
            strokeWidth = strokeWidth
        )
    }
}