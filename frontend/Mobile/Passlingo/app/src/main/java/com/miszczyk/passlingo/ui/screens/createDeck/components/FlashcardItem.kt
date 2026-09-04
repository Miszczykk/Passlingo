package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.borderDefault
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.iconMedium
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun FlashcardItem(
    frontText: String, backText: String, onEditClicked: () -> Unit, onDeleteClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(size = cornerRadiusDefault))
            .background(color = Transparent)
            .border(
                width = borderDefault,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(size = cornerRadiusDefault)
            )
            .padding(all = spaceLarge), verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(weight = 1f)
        ) {
            Text(
                text = frontText,
                fontFamily = vagRoundedBold,
                fontSize = titleMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(height = spaceExtraSmall))

            Text(
                text = backText,
                fontFamily = vagRoundedBold,
                fontSize = body,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

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