package com.miszczyk.passlingo.ui.screens.studyMode.typing

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.ui.components.HintedTextField
import com.miszczyk.passlingo.ui.components.ScreenHeader
import com.miszczyk.passlingo.ui.components.cardSurface
import com.miszczyk.passlingo.ui.screens.studyMode.components.BreatherScreen
import com.miszczyk.passlingo.ui.screens.studyMode.components.LoadingScreen
import com.miszczyk.passlingo.ui.screens.studyMode.components.SessionSummarySection
import com.miszczyk.passlingo.ui.screens.studyMode.typing.components.TypingViewModel
import com.miszczyk.passlingo.ui.screens.studyMode.typing.model.TypeAnswer
import com.miszczyk.passlingo.ui.theme.Dimens.borderDash
import com.miszczyk.passlingo.ui.theme.Dimens.borderGap
import com.miszczyk.passlingo.ui.theme.Dimens.borderThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.Dimens.spaceVeryLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
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

    val borderColor = when(uiState.userAnswer) {
        TypeAnswer.NONE -> MaterialTheme.colorScheme.onBackground
        TypeAnswer.BAD -> MaterialTheme.colorScheme.error
        TypeAnswer.GOOD -> Color(0xFF10B981)
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

    val textDescription = if (check) "Check answer" else "Enter your answer"

    val boxBorderColor = Color(color = 0xFF10B981)

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(height = spaceLarge))
        ScreenHeader(title = uiState.progressText, titleFontSize = titleLarge, onClick = onBack)
        Spacer(modifier = Modifier.height(height = spaceExtraHuge))

        Text(
            text = uiState.currentFront ?: "",
            fontSize = 23.sp,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = vagRoundedBold,
            modifier = Modifier
                .padding(horizontal = spaceExtraLarge)
                .heightIn(max = 150.dp)
                .verticalScroll(state = rememberScrollState()
                )
        )

        Spacer(modifier = Modifier.height(height = spaceExtraLarge))

        BasicTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = spaceExtraLarge).heightIn(max = 150.dp),
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
                        hintText = "Your answer",
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
                .heightIn(max = 150.dp)
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
                            cornerRadius = CornerRadius(cornerRadiusDefault.toPx())
                        )
                    }
                }
                .padding(all = spaceLarge)
                .verticalScroll(state = rememberScrollState())
            ){
                Text(
                    text = uiState.currentBack ?: "",
                    fontSize = titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = vagRoundedLight
                )
            }
        }


        Spacer(modifier = Modifier.weight(1f))
        when (uiState.userAnswer){
            TypeAnswer.NONE -> {
                buttonBeforeAnswer(
                    buttonColor = buttonColor,
                    textColor = textColor,
                    textDescription = textDescription,
                    enabled = check,
                    onClick = {viewModel.checkUserAnswer(userAnswer = uiState.userAnswerState, correctAnswer = uiState.currentBack)}
                )
            }

            TypeAnswer.GOOD -> {
                buttonAfterAnswer(
                    continueLearning = {viewModel.moveToNextCard()},
                    badAnswer = false
                )
            }

            TypeAnswer.BAD -> {
                buttonAfterAnswer(
                    checkAgain = {viewModel.checkAgain(userAnswer = uiState.userAnswerState, correctAnswer = uiState.currentBack)},
                    continueLearning = {viewModel.moveToNextCard()},
                    badAnswer = true
                )
            }
        }
        Spacer(modifier = Modifier.height(height = spaceLarge))
    }
}

@Composable
private fun buttonBeforeAnswer(buttonColor: Color, textColor: Color, textDescription: String, enabled: Boolean, onClick: () -> Unit){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
        shape = RoundedCornerShape(size = cornerRadiusDefault),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        enabled = enabled,
        onClick = {
            onClick()
        }) {
        Text(
            text = textDescription,
            fontSize = titleLarge,
            color = textColor,
            fontFamily = vagRoundedBold,
            modifier = Modifier.padding(vertical = spaceDefault)
        )
    }
}

@Composable
private fun buttonAfterAnswer(checkAgain: () -> Unit = {}, continueLearning: () -> Unit, badAnswer: Boolean){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
    ) {


        Button(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(size = cornerRadiusDefault),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = { continueLearning() }
        ) {
            Text(
                text = if (badAnswer) "Try again" else "Continue",
                fontSize = titleLarge,
                color = MaterialTheme.colorScheme.background,
                fontFamily = vagRoundedBold,
                modifier = Modifier.padding(vertical = spaceDefault)
            )
        }

        if(badAnswer){
            Spacer(modifier = Modifier.height(height = spaceMedium))
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { checkAgain() }
            ) {
                Text(
                    text = "My answer was good",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = vagRoundedBold,
                    modifier = Modifier.padding(vertical = spaceDefault)
                )
            }


        }
    }
}