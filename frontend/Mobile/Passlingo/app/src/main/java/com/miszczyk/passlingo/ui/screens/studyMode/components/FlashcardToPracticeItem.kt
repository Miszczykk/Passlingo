package com.miszczyk.passlingo.ui.screens.studyMode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.style.TextOverflow
import com.miszczyk.passlingo.ui.theme.Dimens.borderDefault
import com.miszczyk.passlingo.ui.theme.Dimens.borderExtraThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.caption
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun FlashcardToPracticeItem(frontText: String, backText: String, attempts: Int){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(size = cornerRadiusDefault))
            .background(color = Transparent)
            .border(
                width = borderDefault,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(size = cornerRadiusDefault)
            )
            .padding(all = spaceLarge), verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(weight = 1f)
        ) {
            Text(
                text = frontText,
                fontFamily = vagRoundedBold,
                fontSize = titleMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(height = spaceExtraSmall))

            Text(
                text = backText,
                fontFamily = vagRoundedBold,
                fontSize = body,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(size = cornerRadiusDefault))
                .background(color = MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                .border(
                    width = borderExtraThin,
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(size = cornerRadiusDefault)
                )
                .padding(all = spaceExtraSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Text(
                text = attempts.toString(),
                fontFamily = vagRoundedBold,
                fontSize = caption,
                color = MaterialTheme.colorScheme.error,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "TRIES",
                fontFamily = vagRoundedLight,
                fontSize = caption,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}