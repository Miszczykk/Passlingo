package com.miszczyk.passlingo.ui.screens.studyMode.flashcards.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusLarge
import com.miszczyk.passlingo.ui.theme.Dimens.iconGiant
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.TextSize.bodyExtraLarge
import com.miszczyk.passlingo.ui.theme.TextSize.bodyLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun BackCard(definition: String) {
    val circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(size = cornerRadiusLarge))
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = cornerRadiusLarge)
            )
            .drawBehind {
                drawCircle(
                    color = circleColor,
                    radius = iconGiant.toPx(),
                    center = Offset(x = 80f, y = size.height - 50f)
                )
            }
            .verticalScroll(state = rememberScrollState())
            .padding(all = spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = stringResource(id = R.string.label_back),
            fontSize = bodyExtraLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontFamily = vagRoundedBold
        )

        Text(
            text = definition,
            fontSize = titleLarge,
            color = MaterialTheme.colorScheme.background,
            fontFamily = vagRoundedLight
        )

        Text(
            text = stringResource(id = R.string.label_tap_to_flip),
            fontSize = bodyLarge,
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = vagRoundedBold
        )
    }
}