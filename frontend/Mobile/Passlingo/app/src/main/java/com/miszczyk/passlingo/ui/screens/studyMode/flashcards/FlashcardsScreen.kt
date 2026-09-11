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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.ThemedDivider
import com.miszczyk.passlingo.ui.screens.studyMode.components.FlashcardToPracticeItem
import com.miszczyk.passlingo.ui.screens.studyMode.components.Header
import com.miszczyk.passlingo.ui.screens.studyMode.flashcards.components.FlashcardsViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Loading...",
                color = MaterialTheme.colorScheme.primary,
                fontFamily = vagRoundedBold
            )
        }
        return
    }

    if (uiState.currentFront == null && uiState.cardsToPractice.isEmpty()) {
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
                    .size(100.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .padding(all = 5.dp)
            )

            Spacer(modifier = Modifier.height(height = 20.dp))

            Text(
                text = "Perfect Session!",
                fontSize = 40.sp,
                fontFamily = vagRoundedBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(height = 10.dp))

            Text(
                text = "You nailed every single card without mistakes.",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontFamily = vagRoundedLight,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(height = 40.dp))

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
                    text = "Finish & Return",
                    fontSize = titleLarge,
                    color = MaterialTheme.colorScheme.background,
                    fontFamily = vagRoundedBold,
                    modifier = Modifier.padding(vertical = spaceDefault)
                )
            }
        }
        return
    }

    if (uiState.currentFront == null && uiState.cardsToPractice.isNotEmpty()) {
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
                        .size(100.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .padding(all = 5.dp)
                )

                Spacer(modifier = Modifier.height(height = 20.dp))

                Text(
                    text = "Needs Practice",
                    fontSize = 40.sp,
                    fontFamily = vagRoundedBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(height = 10.dp))

                Text(
                    text = "These cards took extra tries:",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = vagRoundedLight,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(height = 30.dp))
            }

            items(items = uiState.cardsToPractice, key = {it.id}) {card ->
                FlashcardToPracticeItem(
                    frontText = card.front,
                    backText = card.back,
                    attempts = card.attempts
                )
                Spacer(modifier = Modifier.height(height = spaceLarge))
            }

            item{
                Spacer(modifier = Modifier.height(height = 10.dp))

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
                        text = "Finish & Return",
                        fontSize = titleLarge,
                        color = MaterialTheme.colorScheme.background,
                        fontFamily = vagRoundedBold,
                        modifier = Modifier.padding(vertical = spaceDefault)
                    )
                }
            }

        }
        return
    }


    if (uiState.isBreather) {
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
                    .size(100.dp)
                    .background(
                        color = Color(0xFF3b82f6).copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .padding(all = 10.dp)
            )

            Spacer(modifier = Modifier.height(height = 40.dp))

            Text(
                text = "Take a breather",
                fontSize = 40.sp,
                fontFamily = vagRoundedBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(height = 10.dp))

            Text(
                text = "You've done 10 reviews! Take a short mental break before continuing your learning.",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontFamily = vagRoundedLight,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(height = 40.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spaceExtraLarge),
                shape = RoundedCornerShape(size = cornerRadiusDefault),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    viewModel.continueLearningClicked()
                }) {
                Text(
                    text = "Continue studying",
                    fontSize = titleLarge,
                    color = MaterialTheme.colorScheme.background,
                    fontFamily = vagRoundedBold,
                    modifier = Modifier.padding(vertical = spaceDefault)
                )
            }

            Spacer(modifier = Modifier.height(height = 10.dp))

            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onBack() }
            ) {
                Text(
                    text = "Quit for now",
                    fontSize = 23.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = vagRoundedBold,
                    modifier = Modifier.padding(vertical = spaceDefault)
                )
            }
        }
        return
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(height = spaceLarge))
        Header(title = uiState.progressText, onBack)
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
            .clip(RoundedCornerShape(size = 32.dp))
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 32.dp)
            )
            .drawBehind {
                drawCircle(
                    color = circleColor,
                    radius = 75.dp.toPx(),
                    center = Offset(x = size.width - 80f, y = 50f)
                )
            }
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "FRONT",
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.secondary,
            fontFamily = vagRoundedBold
        )

        Text(
            text = definition,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.background,
            fontFamily = vagRoundedLight
        )

        Text(
            text = "Tap to flip",
            fontSize = 16.sp,
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
            .clip(RoundedCornerShape(size = 32.dp))
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 32.dp)
            )
            .drawBehind {
                drawCircle(
                    color = circleColor,
                    radius = 75.dp.toPx(),
                    center = Offset(x = 80f, y = size.height - 50f)
                )
            }
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "BACK",
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.secondary,
            fontFamily = vagRoundedBold
        )

        Text(
            text = definition,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.background,
            fontFamily = vagRoundedLight
        )

        Text(
            text = "Tap to flip",
            fontSize = 16.sp,
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
                val strokeWidthPx = 1.dp.toPx()
                val dashLengthPx = 8.dp.toPx()
                val gapLengthPx = 6.dp.toPx()
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
            text = "TAP CARD TO REVEAL ANSWER",
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
                .weight(1f),
            shape = RoundedCornerShape(size = cornerRadiusDefault),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            onClick = {
                onIncorrect()
            }
        ) {
            Text(
                text = "Again",
                fontSize = titleLarge,
                color = MaterialTheme.colorScheme.background,
                fontFamily = vagRoundedBold,
                modifier = Modifier.padding(vertical = spaceDefault)
            )
        }

        Button(
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(size = cornerRadiusDefault),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF10B981)
            ),
            onClick = {
                onCorrect()
            }
        ) {
            Text(
                text = "Got it",
                fontSize = titleLarge,
                color = MaterialTheme.colorScheme.background,
                fontFamily = vagRoundedBold,
                modifier = Modifier.padding(vertical = spaceDefault)
            )
        }
    }
}