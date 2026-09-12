package com.miszczyk.passlingo.ui.screens.decks.manageDeck.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.FlashcardRow
import com.miszczyk.passlingo.ui.theme.Dimens.iconMedium

@Composable
fun FlashcardItem(
    frontText: String, backText: String, onEditClicked: () -> Unit, onDeleteClicked: () -> Unit
) {
    FlashcardRow(frontText = frontText, backText = backText) {
        Row {
            IconButton(
                onClick = { onEditClicked() }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.content_desc_edit),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(size = iconMedium)
                )
            }

            IconButton(
                onClick = { onDeleteClicked() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.content_desc_delete),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(size = iconMedium)
                )
            }
        }
    }
}