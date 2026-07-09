package com.miszczyk.passlingo.ui.screens.home.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit

@Composable
fun convertTimeToString(
    rawTime: String,
    numberFont: TextUnit,
    textFont: TextUnit
): AnnotatedString {
    val annotatedTime = buildAnnotatedString {
        rawTime.forEach { char ->
            when {
                char.isDigit() -> {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary, fontSize = numberFont
                        )
                    ) { append(char.toString()) }
                }

                char.isLetter() -> {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.secondary, fontSize = textFont
                        )
                    ) { append(char.toString()) }
                }

                else -> {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Transparent, fontSize = numberFont
                        )
                    ) { append(char.toString()) }
                }
            }
        }
    }
    return annotatedTime
}

fun convertIntToTime(totalSeconds: Long): String{
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return String.format("%02dh %02dm %02ds", hours, minutes, seconds)
}

fun formatDuration(totalSeconds: Long): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format("%02dh %02dm %02ds", hours, minutes, seconds)
    } else if (minutes > 0) {
        String.format("%02dm %02ds", minutes, seconds)
    } else {
        String.format("%02ds", seconds)
    }
}