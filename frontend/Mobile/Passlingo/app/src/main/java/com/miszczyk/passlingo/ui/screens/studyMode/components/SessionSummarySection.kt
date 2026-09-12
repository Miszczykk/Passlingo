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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.screens.studyMode.model.PracticeCardUiModel
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.iconMassive
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.TextSize.headlineLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun SessionSummarySection(cardsToPractice: List<PracticeCardUiModel>, onBack: () -> Unit, modifier: Modifier = Modifier){
    if(cardsToPractice.isEmpty()){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = spaceExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.cup),
                contentDescription = "cup",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(size = iconMassive)
                    .background(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .padding(all = spaceExtraSmall)
            )

            Spacer(modifier = Modifier.height(height = spaceLarge))

            Text(
                text = stringResource(id = R.string.title_perfect_session),
                fontSize = headlineLarge,
                fontFamily = vagRoundedBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(height = spaceMedium))

            Text(
                text = stringResource(id = R.string.message_perfect_session),
                fontSize = titleMedium,
                textAlign = TextAlign.Center,
                fontFamily = vagRoundedLight,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(height = spaceHuge))

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(size = cornerRadiusDefault),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    onBack()
                }) {
                Text(
                    text = stringResource(id = R.string.action_finish_and_return),
                    fontSize = titleLarge,
                    color = MaterialTheme.colorScheme.background,
                    fontFamily = vagRoundedBold,
                    modifier = Modifier.padding(vertical = spaceDefault)
                )
            }
        }
    } else{
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = spaceExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item{
                Spacer(modifier = Modifier.height(height = spaceExtraHuge))
                Icon(
                    painter = painterResource(R.drawable.cup),
                    contentDescription = "cup",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(size = iconMassive)
                        .background(
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .padding(all = spaceExtraSmall)
                )

                Spacer(modifier = Modifier.height(height = spaceLarge))

                Text(
                    text = stringResource(id = R.string.title_needs_practice),
                    fontSize = headlineLarge,
                    fontFamily = vagRoundedBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(height = spaceMedium))

                Text(
                    text = stringResource(id = R.string.message_extra_tries),
                    fontSize = titleMedium,
                    textAlign = TextAlign.Center,
                    fontFamily = vagRoundedLight,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(height = spaceExtraLarge))
            }

            items(items = cardsToPractice, key = {it.id}) {card ->
                FlashcardToPracticeItem(
                    frontText = card.front,
                    backText = card.back,
                    attempts = card.attempts
                )
                Spacer(modifier = Modifier.height(height = spaceLarge))
            }

            item{
                Spacer(modifier = Modifier.height(height = spaceMedium))

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(size = cornerRadiusDefault),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        onBack()
                    }) {
                    Text(
                        text = stringResource(id = R.string.action_finish_and_return),
                        fontSize = titleLarge,
                        color = MaterialTheme.colorScheme.background,
                        fontFamily = vagRoundedBold,
                        modifier = Modifier.padding(vertical = spaceDefault)
                    )
                }
            }
        }
    }
}