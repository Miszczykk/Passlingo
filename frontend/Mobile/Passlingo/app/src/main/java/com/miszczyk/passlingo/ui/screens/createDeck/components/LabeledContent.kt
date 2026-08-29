package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = vagRoundedBold,
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}