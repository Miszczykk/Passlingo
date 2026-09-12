package com.miszczyk.passlingo.ui.screens.studyMode.flashcards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.ScreenHeader
import com.miszczyk.passlingo.ui.components.ThemedDivider
import com.miszczyk.passlingo.ui.screens.studyMode.components.BreatherScreen
import com.miszczyk.passlingo.ui.screens.studyMode.components.LoadingScreen
import com.miszczyk.passlingo.ui.screens.studyMode.components.SessionSummarySection
import com.miszczyk.passlingo.ui.screens.studyMode.flashcards.components.FlashcardsViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.borderDash
import com.miszczyk.passlingo.ui.theme.Dimens.borderGap
import com.miszczyk.passlingo.ui.theme.Dimens.borderThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusLarge
import com.miszczyk.passlingo.ui.theme.Dimens.iconGiant
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.bodyExtraLarge
import com.miszczyk.passlingo.ui.theme.TextSize.bodyLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun FlashcardScreen(
    modifier: Modifier = Modifier,
    deckId: String,
    rounds: Int,
    onBack: () -> Unit,
    viewModel: FlashcardsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = deckId) {
        viewModel.startSession(deckId, rounds = rounds)
    }

    if (uiState.isLoading) {
        LoadingScreen()
        return
    }

    if(uiState.currentFront == null){
        SessionSummarySection(
            cardsToPractice = uiState.cardsToPractice,
            onBack = onBack,
            modifier = modifier
        )
        return
    }

    if (uiState.isBreather) {
        BreatherScreen(
            continueLearning ={ viewModel.continueLearningClicked()},
            onBack = onBack,
            modifier = modifier
        )
        return
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(height = spaceLarge))
        ScreenHeader(title = uiState.progressText, titleFontSize = titleLarge, onClick = onBack)
        Spacer(modifier = Modifier.height(height = spaceExtraHuge))

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = spaceExtraLarge)
                .clickable { viewModel.flipCard() }
        ) {

            if (!uiState.isFlipped) {
                FrontCard(definition = uiState.currentFront ?: "")
            } else {
                BackCard(definition = uiState.currentBack ?: "")
            }
        }

        Spacer(modifier = Modifier.height(height = spaceExtraHuge))

        ThemedDivider(colorLine = MaterialTheme.colorScheme.onSecondary)
        Spacer(modifier = Modifier.height(height = spaceLarge))
        if (!uiState.isFlipped) {
            FrontButton(onClick = { viewModel.flipCard() })
        } else {
            BackButtons(
                onCorrect = { viewModel.answerCard(isCorrect = true) },
                onIncorrect = { viewModel.answerCard(isCorrect = false) }
            )
        }
        Spacer(modifier = Modifier.height(height = spaceLarge))
    }
}


@Composable
private fun FrontCard(definition: String) {
    val circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(size = cornerRadiusLarge))
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = cornerRadiusLarge)
            )
            .drawBehind {
                drawCircle(
                    color = circleColor,
                    radius = iconGiant.toPx(),
                    center = Offset(x = size.width - 80f, y = 50f)
                )
            }
            .padding(all = spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.label_front),
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

@Composable
private fun BackCard(definition: String) {
    val circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(size = cornerRadiusLarge))
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
            .padding(all = spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
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

@Composable
private fun FrontButton(onClick: () -> Unit) {
    val borderColor = MaterialTheme.colorScheme.onSurface
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge)
            .drawWithCache {
                val strokeWidthPx = borderThin.toPx()
                val dashLengthPx = borderDash.toPx()
                val gapLengthPx = borderGap.toPx()
                onDrawWithContent {
                    drawContent()

                    drawRoundRect(
                        color = borderColor,
                        style = Stroke(
                            width = strokeWidthPx,
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(dashLengthPx, gapLengthPx),
                                phase = 0f
                            )
                        ),
                        cornerRadius = CornerRadius(cornerRadiusDefault.toPx())
                    )
                }
            },
        shape = RoundedCornerShape(size = cornerRadiusDefault),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = { onClick() }
    ) {
        Text(
            text = stringResource(id = R.string.label_tap_card_to_reveal),
            fontSize = titleLarge,
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = vagRoundedBold,
            modifier = Modifier.padding(vertical = spaceDefault)
        )
    }
}

@Composable
private fun BackButtons(
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