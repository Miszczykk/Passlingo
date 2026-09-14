package com.miszczyk.passlingo.ui.screens.studyMode.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.miszczyk.passlingo.ui.screens.studyMode.components.BaseStudyScreen
import com.miszczyk.passlingo.ui.screens.studyMode.model.TypeAnswer
import com.miszczyk.passlingo.ui.screens.studyMode.quiz.components.AnswerCard
import com.miszczyk.passlingo.ui.screens.studyMode.quiz.viewmodel.QuizViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.iconGiant
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    deckId: String,
    rounds: Int,
    onBack: () -> Unit,
    viewModel: QuizViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.startSession(deckId, rounds)
    }

    BaseStudyScreen(
        uiState = uiState,
        onBack = onBack,
        onContinueBreather = { viewModel.continueLearningClicked() },
        modifier = modifier
    ) {
        val circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
        val isAnswered = uiState.userAnswer != TypeAnswer.NONE

        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .verticalScroll(state = rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spaceExtraLarge)
                    .clip(shape = RoundedCornerShape(size = cornerRadiusDefault))
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(size = cornerRadiusDefault)
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
                )
            }

            Spacer(modifier = Modifier.height(height = spaceExtraLarge))

            uiState.options.forEachIndexed { index, option ->
                val isCorrectAnswer = option == uiState.currentBack
                val isSelectedAnswer = option == uiState.selectedAnswer

                val shouldShow = !isAnswered || isCorrectAnswer || isSelectedAnswer

                if (shouldShow) {
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
                                viewModel.checkUserAnswer(selectedText = option)
                            }
                        })
                }
            }
        }
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
        Spacer(modifier = Modifier.height(height = spaceLarge))
    }
}