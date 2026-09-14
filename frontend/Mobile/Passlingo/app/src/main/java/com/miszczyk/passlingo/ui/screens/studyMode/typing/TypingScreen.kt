package com.miszczyk.passlingo.ui.screens.studyMode.typing

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.HintedTextField
import com.miszczyk.passlingo.ui.components.cardSurface
import com.miszczyk.passlingo.ui.screens.studyMode.components.BaseStudyScreen
import com.miszczyk.passlingo.ui.screens.studyMode.model.TypeAnswer
import com.miszczyk.passlingo.ui.screens.studyMode.typing.components.ButtonAfterAnswer
import com.miszczyk.passlingo.ui.screens.studyMode.typing.components.ButtonBeforeAnswer
import com.miszczyk.passlingo.ui.screens.studyMode.typing.viewmodel.TypingViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.borderDash
import com.miszczyk.passlingo.ui.theme.Dimens.borderGap
import com.miszczyk.passlingo.ui.theme.Dimens.borderThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.maxHeightCardContent
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceVeryLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.TextSize.titleMediumLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun TypingScreen(
    modifier: Modifier = Modifier,
    deckId: String,
    rounds: Int,
    onBack: () -> Unit,
    viewModel: TypingViewModel = viewModel()
    ) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = deckId) {
        viewModel.startSession(deckId, rounds = rounds)
    }

    BaseStudyScreen(
        uiState = uiState,
        onBack = onBack,
        onContinueBreather = { viewModel.continueLearningClicked() },
        modifier = modifier
    ) {
        val borderColor = when(uiState.userAnswer) {
            TypeAnswer.NONE -> MaterialTheme.colorScheme.onBackground
            TypeAnswer.BAD -> MaterialTheme.colorScheme.error
            TypeAnswer.GOOD -> Color(color = 0xFF10B981)
        }

        val check = uiState.userAnswerState.text.toString().isNotBlank()

        val buttonColor by animateColorAsState(
            targetValue = if (check) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            label = "buttonColor"
        )

        val textColor by animateColorAsState(
            targetValue = if (check) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSecondary,
            label = "textColor"
        )

        val textDescription = if (check) stringResource(id = R.string.action_check_answer) else stringResource(id = R.string.prompt_enter_your_answer)

        val boxBorderColor = Color(color = 0xFF10B981)

        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .verticalScroll(state = rememberScrollState())
        ) {
            Text(
                text = uiState.currentFront ?: "",
                fontSize = titleMediumLarge,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = vagRoundedBold,
                modifier = Modifier
                    .padding(horizontal = spaceExtraLarge)
            )

            Spacer(modifier = Modifier.height(height = spaceExtraLarge))

            BasicTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = spaceExtraLarge).heightIn(max = maxHeightCardContent),
                state = uiState.userAnswerState,
                readOnly = uiState.userAnswer != TypeAnswer.NONE,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password
                ),
                textStyle = TextStyle(
                    fontFamily = vagRoundedLight,
                    fontSize = titleMedium,
                    color = MaterialTheme.colorScheme.primary
                ),
                decorator = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .cardSurface(borderColor = borderColor)
                            .padding(horizontal = spaceExtraLarge, vertical = spaceVeryLarge),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        HintedTextField(
                            state = uiState.userAnswerState,
                            hintText = stringResource(id = R.string.prompt_your_answer_hint),
                            fontFamily = vagRoundedLight
                        )
                        innerTextField()
                    }
                },
            )

            if(uiState.userAnswer == TypeAnswer.BAD){
                Spacer(modifier = Modifier.height(height = spaceExtraLarge))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spaceExtraLarge)
                    .drawWithCache {
                        val strokeWidthPx = borderThin.toPx()
                        val dashLengthPx = borderDash.toPx()
                        val gapLengthPx = borderGap.toPx()
                        onDrawWithContent {
                            drawContent()

                            drawRoundRect(
                                color = boxBorderColor,
                                style = Stroke(
                                    width = strokeWidthPx,
                                    pathEffect = PathEffect.dashPathEffect(
                                        intervals = floatArrayOf(dashLengthPx, gapLengthPx),
                                        phase = 0f
                                    )
                                ),
                                cornerRadius = CornerRadius(x = cornerRadiusDefault.toPx())
                            )
                        }
                    }
                    .padding(all = spaceLarge)
                ){
                    Text(
                        text = uiState.currentBack ?: "",
                        fontSize = titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = vagRoundedLight
                    )
                }
            }
            Spacer(modifier = Modifier.height(height = spaceExtraLarge))
        }

        when (uiState.userAnswer){
            TypeAnswer.NONE -> {
                ButtonBeforeAnswer(
                    buttonColor = buttonColor,
                    textColor = textColor,
                    textDescription = textDescription,
                    enabled = check,
                    onClick = {viewModel.checkUserAnswer(userAnswer = uiState.userAnswerState, correctAnswer = uiState.currentBack)}
                )
            }

            TypeAnswer.GOOD -> {
                ButtonAfterAnswer(
                    continueLearning = {viewModel.moveToNextCard()},
                    badAnswer = false
                )
            }

            TypeAnswer.BAD -> {
                ButtonAfterAnswer(
                    checkAgain = {viewModel.checkAgain(userAnswer = uiState.userAnswerState, correctAnswer = uiState.currentBack)},
                    continueLearning = {viewModel.moveToNextCard()},
                    badAnswer = true
                )
            }
        }
        Spacer(modifier = Modifier.height(height = spaceLarge))
    }
}