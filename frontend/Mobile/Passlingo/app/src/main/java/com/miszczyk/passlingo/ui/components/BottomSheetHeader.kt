package com.miszczyk.passlingo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetHeader(
    label: String, onCloseClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = vagRoundedBold
        )
        IconButton(
            onClick = {
                onCloseClicked()
            }, modifier = Modifier.background(
                color = MaterialTheme.colorScheme.onBackground, shape = CircleShape
            )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.content_desc_close),
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}