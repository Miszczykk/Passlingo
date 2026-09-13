package com.miszczyk.passlingo.ui.screens.studyMode.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.ScreenHeader
import com.miszczyk.passlingo.ui.components.cardSurface
import com.miszczyk.passlingo.ui.screens.studyMode.components.BreatherScreen
import com.miszczyk.passlingo.ui.screens.studyMode.components.LoadingScreen
import com.miszczyk.passlingo.ui.screens.studyMode.components.SessionSummarySection
import com.miszczyk.passlingo.ui.screens.studyMode.quiz.components.QuizViewModel
import com.miszczyk.passlingo.ui.screens.studyMode.quiz.model.TypeAnswer
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusLarge
import com.miszczyk.passlingo.ui.theme.Dimens.iconGiant
import com.miszczyk.passlingo.ui.theme.Dimens.maxHeightCardContent
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    deckId: String,
    rounds: Int,
    onBack: () -> Unit,
    viewModel: QuizViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startSession(deckId, rounds)
    }

    if (uiState.isLoading) {
        LoadingScreen()
        return
    }

    if (uiState.currentFront == null) {
        SessionSummarySection(
            cardsToPractice = uiState.cardsToPractice,
            onBack = onBack,
            modifier = modifier
        )
        return
    }

    if (uiState.isBreather) {
        BreatherScreen(
            continueLearning = { viewModel.continueLearningClicked() },
            onBack = onBack,
            modifier = modifier
        )
        return
    }

    val circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
    val isAnswered = uiState.userAnswer != TypeAnswer.NONE

    Column(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(height = spaceLarge))
        ScreenHeader(title = uiState.progressText, titleFontSize = titleLarge, onClick = onBack)
        Spacer(modifier = Modifier.height(height = spaceExtraHuge))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spaceExtraLarge)
                .clip(shape = RoundedCornerShape(size = cornerRadiusLarge))
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
        ) {
            Text(
                text = uiState.currentFront ?: "",
                fontSize = titleLarge,
                color = MaterialTheme.colorScheme.background,
                fontFamily = vagRoundedBold,
                modifier = Modifier
                    .padding(horizontal = spaceExtraLarge)
                    .heightIn(max = maxHeightCardContent)
                    .verticalScroll(state = rememberScrollState())
            )
        }

        Spacer(modifier = Modifier.height(height = spaceExtraLarge))

        uiState.options.forEachIndexed { index, option ->
            val isCorrectAnswer = option == uiState.currentBack
            val isSelectedAnswer = option == uiState.selectedAnswer

            val shouldShow = !isAnswered || isCorrectAnswer || isSelectedAnswer

            if(shouldShow){
                val borderColor = when {
                    !isAnswered -> MaterialTheme.colorScheme.onBackground
                    isCorrectAnswer -> Color(color = 0xFF10B981)
                    else -> MaterialTheme.colorScheme.error
                }
                AnswerCard(
                    text = option,
                    textColor = MaterialTheme.colorScheme.primary,
                    borderColor = borderColor,
                    onClick = {
                        if (!isAnswered) {
                            viewModel.checkUserAnswer(option)
                        }
                    }
                )
            }
            }

            Spacer(modifier = Modifier.weight(1f))
            if (isAnswered) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spaceExtraLarge),
                    shape = RoundedCornerShape(size = cornerRadiusDefault),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        viewModel.moveToNextCard()
                    }) {
                    Text(
                        text = stringResource(id = R.string.action_continue),
                        fontSize = titleLarge,
                        color = MaterialTheme.colorScheme.background,
                        fontFamily = vagRoundedBold,
                        modifier = Modifier.padding(vertical = spaceDefault)
                    )
                }
            }
        }
    }

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