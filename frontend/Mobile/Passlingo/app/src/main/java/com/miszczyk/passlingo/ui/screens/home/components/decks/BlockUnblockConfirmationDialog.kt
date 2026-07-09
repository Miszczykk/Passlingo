package com.miszczyk.passlingo.ui.screens.home.components.decks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun BlockUnblockConfirmationDialog(
    cancelClicked: () -> Unit,
    acceptClicked: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            cancelClicked()
        },
        title = {
            Text(
                text = "Unlock app?", fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = vagRoundedBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Unlocking this app requires you to pay with you earned study time.",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = vagRoundedLight,
                textAlign = TextAlign.Center,
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                TextButton(
                    onClick = { cancelClicked() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = vagRoundedBold,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }

                TextButton(
                    onClick = { acceptClicked() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Pay & Unlock",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = vagRoundedBold,
                    )
                }
            }
        }
    )
}