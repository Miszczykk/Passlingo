package com.miszczyk.passlingo.ui.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit

@Composable
fun styledTimeText(
    rawTime: String, numberFont: TextUnit, textFont: TextUnit
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