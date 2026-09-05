package com.miszczyk.passlingo.ui.screens.home.components.decks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.ThemedDivider
import com.miszczyk.passlingo.ui.theme.Dimens.borderDefault
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.iconExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.iconMedium
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMediumLarge
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckBottomSheet(
    sheetState: SheetState,
    deckIcon: Int,
    deckName: String,
    flashcardCount: Int,
    onDismissRequest: () -> Unit,
    onStudyClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest, sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val coroutineScope = rememberCoroutineScope()

            Header(
                iconResId = deckIcon,
                deckName = deckName,
                flashcardCount = flashcardCount,
                onCloseClicked = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onDismissRequest()
                    }
                }
            )

            Spacer(modifier = Modifier.height(height = spaceExtraLarge))
            ThemedDivider(colorLine = MaterialTheme.colorScheme.onSecondary)
            Spacer(modifier = Modifier.height(height = spaceExtraLarge))

            OptionButton(
                icon = Icons.Outlined.School,
                iconColor = MaterialTheme.colorScheme.secondary,
                textDescription = stringResource(id = R.string.action_study_now),
                textColor = MaterialTheme.colorScheme.background,
                backgroundColor = MaterialTheme.colorScheme.primary,
                onClick = { onStudyClicked() }
            )

            Spacer(modifier = Modifier.height(height = spaceMediumLarge))

            OptionButton(
                icon = Icons.Outlined.Edit,
                iconColor = MaterialTheme.colorScheme.primary,
                textDescription = stringResource(id = R.string.action_edit_deck),
                textColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.background,
                borderColor = MaterialTheme.colorScheme.onBackground,
                onClick = { onEditClicked() }
            )

            Spacer(modifier = Modifier.height(height = spaceMediumLarge))

            OptionButton(
                icon = Icons.Outlined.DeleteOutline,
                iconColor = MaterialTheme.colorScheme.background,
                textDescription = stringResource(id = R.string.action_delete_deck),
                textColor = MaterialTheme.colorScheme.background,
                backgroundColor = MaterialTheme.colorScheme.error,
                onClick = { onDeleteClicked() }
            )

            Spacer(modifier = Modifier.height(height = spaceDefault))
        }
    }
}

@Composable
private fun Header(
    iconResId: Int, deckName: String, flashcardCount: Int, onCloseClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(weight = 1f, fill = false),) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(size = cornerRadiusDefault)
                )
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = stringResource(id = R.string.content_desc_deck_icon),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(size = iconExtraHuge)
                        .padding(all = spaceExtraSmall)
                )
            }

            Spacer(modifier = Modifier.width(width = spaceLarge))

            Column(
                modifier = Modifier.weight(weight = 1f, fill = false),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = deckName,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = titleMedium,
                    fontFamily = vagRoundedBold,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = pluralStringResource(id = R.plurals.label_cards_count, count = flashcardCount, flashcardCount),
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = body,
                    fontFamily = vagRoundedBold,
                )
            }
        }
        Spacer(modifier = Modifier.width(width = spaceLarge))

        IconButton(
            onClick = {
                onCloseClicked()
            }, modifier = Modifier.background(
                color = MaterialTheme.colorScheme.onBackground, shape = CircleShape
            )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.content_desc_close),
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Composable
private fun OptionButton(
    icon: ImageVector,
    iconColor: Color,
    textDescription: String,
    textColor: Color,
    backgroundColor: Color,
    borderColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge)
            .border(
                width = borderDefault,
                color = borderColor,
                shape = RoundedCornerShape(size = cornerRadiusDefault)
            ),
        shape = RoundedCornerShape(size = cornerRadiusDefault),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        onClick = {
            onClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spaceDefault),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = R.string.content_desc_deck_icon),
                tint = iconColor,
                modifier = Modifier.size(size = iconMedium)
            )

            Spacer(modifier = Modifier.width(width = spaceMedium))

            Text(
                text = textDescription,
                fontSize = titleLarge,
                color = textColor,
                fontFamily = vagRoundedBold,
            )
        }
    }
}