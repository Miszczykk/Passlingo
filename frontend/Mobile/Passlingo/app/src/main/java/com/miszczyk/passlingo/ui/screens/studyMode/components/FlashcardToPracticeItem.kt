package com.miszczyk.passlingo.ui.screens.studyMode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.FlashcardRow
import com.miszczyk.passlingo.ui.theme.Dimens.borderExtraThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.TextSize.caption
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun FlashcardToPracticeItem(frontText: String, backText: String, attempts: Int){
    FlashcardRow(frontText = frontText, backText = backText) {
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
                text = stringResource(id = R.string.label_tries),
                fontFamily = vagRoundedLight,
                fontSize = caption,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}