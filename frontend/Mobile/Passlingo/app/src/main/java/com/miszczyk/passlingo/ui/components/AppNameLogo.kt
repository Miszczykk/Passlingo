package com.miszczyk.passlingo.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import com.miszczyk.passlingo.R

@Composable
fun AppNameLogo(smallSize: TextUnit, bigSize: TextUnit): AnnotatedString {
    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary, fontSize = smallSize
            )
        ) {
            append(stringResource(id = R.string.app_name))
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.secondary, fontSize = bigSize
            )
        ) {
            append('.')
        }
    }
}