package com.miszczyk.passlingo.ui.screens.studyMode.quiz.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.miszczyk.passlingo.ui.components.cardSurface
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun AnswerCard(text: String, textColor: Color, borderColor: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge)
            .padding(bottom = spaceDefault)
            .clip(RoundedCornerShape(cornerRadiusDefault))
            .clickable(onClick = onClick)
            .cardSurface(
                borderColor = borderColor
            )
            .padding(all = spaceLarge)
    ) {
        Text(
            text = text,
            fontSize = titleMedium,
            color = textColor,
            fontFamily = vagRoundedLight
        )
    }
}