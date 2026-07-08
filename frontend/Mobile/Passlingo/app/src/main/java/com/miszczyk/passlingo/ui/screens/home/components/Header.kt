package com.miszczyk.passlingo.ui.screens.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun Header() {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontSize = 55.sp)) {
            append(stringResource(R.string.app_name))
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.secondary, fontSize = 110.sp
            )
        ) {
            append('.')
        }
    }
    Text(text = annotatedText, textAlign = TextAlign.Center, fontFamily = vagRoundedBold)
}