package com.miszczyk.passlingo.ui.screens.home.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.ui.screens.home.model.DialogItem
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun DialogComponent(dialog: DialogItem, onConfirm: () -> Unit, onCancel: () -> Unit) {
    val onConfirmTextColor = if (dialog.onCancelText != null) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.background
    }
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(
                text = dialog.title, fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = vagRoundedBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column() {
                Text(
                    text = dialog.message,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = vagRoundedLight,
                    textAlign = TextAlign.Center,
                )
                if (dialog.extraContent != null) {
                    Spacer(modifier = Modifier.height(10.dp))

                    dialog.extraContent()
                }
            }

        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (dialog.onCancelText != null) {
                    TextButton(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = dialog.onCancelText,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = vagRoundedBold,
                            modifier = Modifier.padding(vertical = 5.dp)
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
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = dialog.onConfirmText,
                        fontSize = 15.sp,
                        color = onConfirmTextColor,
                        fontFamily = vagRoundedBold,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }
        }
    )
}