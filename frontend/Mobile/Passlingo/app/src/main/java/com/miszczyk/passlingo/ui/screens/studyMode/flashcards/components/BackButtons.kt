package com.miszczyk.passlingo.ui.screens.studyMode.flashcards.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun BackButtons(
    onCorrect: () -> Unit,
    onIncorrect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
        horizontalArrangement = Arrangement.spacedBy(spaceLarge)
    ) {
        Button(
            modifier = Modifier
                .weight(weight = 1f),
            shape = RoundedCornerShape(size = cornerRadiusDefault),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            onClick = {
                onIncorrect()
            }
        ) {
            Text(
                text = stringResource(id = R.string.action_again),
                fontSize = titleLarge,
                color = MaterialTheme.colorScheme.background,
                fontFamily = vagRoundedBold,
                modifier = Modifier.padding(vertical = spaceDefault)
            )
        }

        Button(
            modifier = Modifier
                .weight(weight = 1f),
            shape = RoundedCornerShape(size = cornerRadiusDefault),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF10B981)
            ),
            onClick = {
                onCorrect()
            }
        ) {
            Text(
                text = stringResource(id = R.string.action_got_it),
                fontSize = titleLarge,
                color = MaterialTheme.colorScheme.background,
                fontFamily = vagRoundedBold,
                modifier = Modifier.padding(vertical = spaceDefault)
            )
        }
    }
}