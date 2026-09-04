package com.miszczyk.passlingo.ui.screens.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.TextSize.displayLarge
import com.miszczyk.passlingo.ui.theme.TextSize.displaySmall
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun Header(
    modifier: Modifier = Modifier
) {
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary, fontSize = displaySmall
            )
        ) {
            append(stringResource(id = R.string.app_name))
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.secondary, fontSize = displayLarge
            )
        ) {
            append('.')
        }
    }
    Text(text = annotatedText, textAlign = TextAlign.Center, fontFamily = vagRoundedBold)
}