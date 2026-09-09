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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.BottomSheetHeader
import com.miszczyk.passlingo.ui.components.ThemedDivider
import com.miszczyk.passlingo.ui.theme.Dimens.borderDefault
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusMedium
import com.miszczyk.passlingo.ui.theme.Dimens.iconHuge
import com.miszczyk.passlingo.ui.theme.Dimens.iconLarge
import com.miszczyk.passlingo.ui.theme.Dimens.sizeIndicator
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMediumLarge
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight
import com.miszczyk.passlingo.ui.util.rememberSheetCloseHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySettingsBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onStartSessionClicked: () -> Unit,
){
    val closeSheet = rememberSheetCloseHandler(sheetState, onDismissRequest)
    var selectedRound by remember { mutableIntStateOf(value = 1) }

    ModalBottomSheet(
        onDismissRequest = closeSheet, sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomSheetHeader(label = stringResource(id = R.string.label_study_settings)) {
                closeSheet()
            }

            Spacer(modifier = Modifier.height(height = spaceExtraLarge))
            ThemedDivider(colorLine = MaterialTheme.colorScheme.onSecondary)
            Spacer(modifier = Modifier.height(height = spaceExtraLarge))

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = spaceExtraLarge), horizontalArrangement = Arrangement.SpaceBetween){
                Text(
                    text = stringResource(id = R.string.label_target_rounds),
                    fontSize = titleMedium,
                    fontFamily = vagRoundedBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(size = sizeIndicator)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                ){
                    Text(
                        text = "$selectedRound",
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = vagRoundedBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(height = spaceMediumLarge))

            Text(
                text = stringResource(id = R.string.desc_target_rounds),
                fontSize = body,
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = vagRoundedLight,
                modifier = Modifier.padding(horizontal = spaceExtraLarge)
            )
            Spacer(modifier = Modifier.height(height = spaceLarge))

            TargetRoundsSelector(
                currentSelectedRound = selectedRound,
                onRoundSelected = {round -> selectedRound = round}
            )

            Spacer(modifier = Modifier.height(height = spaceExtraLarge))

            BottomButton(onClick = onStartSessionClicked)

            Spacer(modifier = Modifier.height(height = spaceDefault))
        }
    }
}
@Composable
private fun BottomButton(
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
        shape = RoundedCornerShape(size = cornerRadiusDefault),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
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
                imageVector = Icons.Outlined.PlayArrow,
                contentDescription = stringResource(id = R.string.content_desc_deck_icon),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(size = iconLarge)
            )

            Spacer(modifier = Modifier.width(width = spaceMedium))

            Text(
                text = stringResource(id = R.string.action_start_session),
                fontSize = titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontFamily = vagRoundedBold,
            )
        }
    }
}

@Composable
private fun TargetRoundsSelector(
    currentSelectedRound: Int,
    onRoundSelected: (Int) -> Unit
){
    val rounds = listOf(1, 2, 3, 4, 5, 6)

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = spaceExtraLarge),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        rounds.forEach { round ->
            TargetRoundItem(
                roundNumber = round,
                isSelected = round == currentSelectedRound,
                onClick = {onRoundSelected(round)}
            )
        }
    }
}

@Composable
private fun TargetRoundItem(
    roundNumber: Int,
    isSelected: Boolean,
    onClick: () -> Unit
){
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background
    val borderColor = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.3f)
    val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size = iconHuge)
            .clip(shape = RoundedCornerShape(size = cornerRadiusMedium))
            .background(color = backgroundColor)
            .border(
                width = borderDefault,
                color = borderColor,
                shape = RoundedCornerShape(size = cornerRadiusMedium)
            )
            .selectable(
                selected = isSelected,
                onClick = onClick
            )
    ){
        Text(
            text = roundNumber.toString(),
            fontSize = titleMedium,
            color = textColor,
            fontFamily = vagRoundedBold
        )
    }
}