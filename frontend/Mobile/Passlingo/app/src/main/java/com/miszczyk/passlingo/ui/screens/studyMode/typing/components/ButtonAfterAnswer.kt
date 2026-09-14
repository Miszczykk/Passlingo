package com.miszczyk.passlingo.ui.screens.studyMode.typing.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleSmall
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun ButtonAfterAnswer(
    checkAgain: () -> Unit = {}, continueLearning: () -> Unit, badAnswer: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = cornerRadiusDefault),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = { continueLearning() }
        ) {
            Text(
                text = if (badAnswer) stringResource(id = R.string.action_try_again) else stringResource(
                    id = R.string.action_continue
                ),
                fontSize = titleLarge,
                color = MaterialTheme.colorScheme.background,
                fontFamily = vagRoundedBold,
                modifier = Modifier.padding(vertical = spaceDefault)
            )
        }

        if (badAnswer) {
            Spacer(modifier = Modifier.height(height = spaceMedium))
            TextButton(
                modifier = Modifier.fillMaxWidth(), onClick = { checkAgain() }
            ) {
                Text(
                    text = stringResource(id = R.string.action_my_answer_was_good),
                    fontSize = titleSmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = vagRoundedBold,
                    modifier = Modifier.padding(vertical = spaceDefault)
                )
            }
        }
    }
}