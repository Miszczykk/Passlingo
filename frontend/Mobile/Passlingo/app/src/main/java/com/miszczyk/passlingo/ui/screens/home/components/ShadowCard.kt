package com.miszczyk.passlingo.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.miszczyk.passlingo.ui.screens.home.util.convertTimeToString
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.elevationSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.letterSpacingLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBlack
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun ShadowCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge)
            .shadow(elevation = elevationSmall, shape = RoundedCornerShape(cornerRadiusDefault))
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(cornerRadiusDefault)
            )
            .padding(vertical = spaceLarge)
    ) {
        content()
    }
}

@Composable
fun TitleToCard(titleText: String, titleFontSize: TextUnit) {
    Text(
        text = titleText,
        fontSize = titleFontSize,
        textAlign = TextAlign.Center,
        fontFamily = vagRoundedLight,
        fontWeight = FontWeight.Bold,
        letterSpacing = letterSpacingLarge,
        color = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
fun TimeToCard(
    timeText: String,
    numberFontSize: TextUnit,
    textFontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    Text(
        text = convertTimeToString(timeText, numberFontSize, textFontSize),
        textAlign = TextAlign.Center,
        fontFamily = vagRoundedBlack,
        maxLines = 1,
        lineHeight = numberFontSize,
        modifier = modifier,
    )
}