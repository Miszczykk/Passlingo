package com.miszczyk.passlingo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.miszczyk.passlingo.ui.model.DialogItem
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight
import androidx.compose.ui.window.DialogProperties

@Composable
fun DialogComponent(
    dialog: DialogItem, onConfirm: () -> Unit, onCancel: () -> Unit
) {
    val confirmTextColor = dialog.onConfirmTextColor ?: MaterialTheme.colorScheme.secondary

    val dialogProperties = if (dialog.isWide) {
        DialogProperties(usePlatformDefaultWidth = false)
    } else {
        DialogProperties()
    }

    val dialogModifier = if (dialog.isWide) {
        Modifier.fillMaxWidth(fraction = 0.92f)
    } else {
        Modifier
    }

    AlertDialog(
        properties = dialogProperties,
        modifier = dialogModifier,
        onDismissRequest = onCancel,
        title = {
            Text(
                text = dialog.title,
                fontSize = titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = vagRoundedBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                Text(
                    text = dialog.message,
                    fontSize = body,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = vagRoundedLight,
                    textAlign = TextAlign.Center,
                )
                if (dialog.extraContent != null) {
                    Spacer(modifier = Modifier.height(height = spaceMedium))
                    dialog.extraContent()
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spaceLarge)
            ) {
                if (dialog.onCancelText != null) {
                    TextButton(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1f),
                        shape = RoundedCornerShape(size = cornerRadiusSmall)
                    ) {
                        Text(
                            text = dialog.onCancelText,
                            fontSize = body,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = vagRoundedBold,
                            modifier = Modifier.padding(vertical = spaceExtraSmall)
                        )
                    }
                }

                TextButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(size = cornerRadiusSmall)
                ) {
                    Text(
                        text = dialog.onConfirmText,
                        fontSize = body,
                        color = confirmTextColor,
                        fontFamily = vagRoundedBold,
                        modifier = Modifier.padding(vertical = spaceExtraSmall)
                    )
                }
            }
        }
    )
}