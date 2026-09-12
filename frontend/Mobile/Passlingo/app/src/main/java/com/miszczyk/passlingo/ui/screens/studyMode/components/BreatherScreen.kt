package com.miszczyk.passlingo.ui.screens.studyMode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.iconMassive
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.TextSize.headlineLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.TextSize.titleMediumLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun BreatherScreen(continueLearning: () -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.coffee),
            contentDescription = "coffee",
            tint = Color(0xFF3b82f6),
            modifier = Modifier
                .size(size = iconMassive)
                .background(
                    color = Color(0xFF3b82f6).copy(alpha = 0.2f),
                    shape = CircleShape
                )
                .padding(all = spaceMedium)
        )

        Spacer(modifier = Modifier.height(height = spaceHuge))

        Text(
            text = stringResource(id = R.string.title_take_a_breather),
            fontSize = headlineLarge,
            fontFamily = vagRoundedBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(height = spaceMedium))

        Text(
            text = stringResource(id = R.string.message_take_a_breather),
            fontSize = titleMedium,
            textAlign = TextAlign.Center,
            fontFamily = vagRoundedLight,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(height = spaceHuge))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spaceExtraLarge),
            shape = RoundedCornerShape(size = cornerRadiusDefault),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                continueLearning()
            }) {
            Text(
                text = stringResource(id = R.string.action_continue_studying),
                fontSize = titleLarge,
                color = MaterialTheme.colorScheme.background,
                fontFamily = vagRoundedBold,
                modifier = Modifier.padding(vertical = spaceDefault)
            )
        }

        Spacer(modifier = Modifier.height(height = spaceMedium))

        TextButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { onBack() }
        ) {
            Text(
                text = stringResource(id = R.string.action_quit_for_now),
                fontSize = titleMediumLarge,
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = vagRoundedBold,
                modifier = Modifier.padding(vertical = spaceDefault)
            )
        }
    }
}