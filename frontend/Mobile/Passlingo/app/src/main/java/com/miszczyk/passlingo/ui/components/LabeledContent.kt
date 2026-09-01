package com.miszczyk.passlingo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.TextSize.titleSmall
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun LabeledContent(
    modifier: Modifier = Modifier,
    label: String,
    horizontal:  Alignment.Horizontal? = Alignment.Start,
    content: @Composable () -> Unit,

){
    Column(modifier = modifier, horizontalAlignment = horizontal ?: Alignment.Start) {
        Text(
            text = label.uppercase(),
            fontSize = titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = vagRoundedBold,
        )
        Spacer(modifier = Modifier.height(spaceMedium))
        content()
    }
}