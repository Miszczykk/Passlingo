package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge

@Composable
fun HorizontalDivider(){
    val lineColor = MaterialTheme.colorScheme.onSecondary
    Canvas(modifier = Modifier.fillMaxWidth()) {
        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = 0f),
            color = lineColor,
            strokeWidth = 1f
        )
    }
}