package com.miszczyk.passlingo.ui.screens.home.components.decks

import androidx.compose.foundation.layout.fillMaxWidth
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
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun BlockWithoutTime(
    Clicked: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            Clicked()
        },
        title = {
            Text(
                text = "Not enough time", fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = vagRoundedBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "You don't have enough earned study time to unlock this app (requires 1 hour).",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = vagRoundedLight,
                textAlign = TextAlign.Center,
            )
        },
        confirmButton = {
            TextButton(
                onClick = { Clicked() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Keep studying",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.background,
                    fontFamily = vagRoundedBold,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }
        }
    )
}