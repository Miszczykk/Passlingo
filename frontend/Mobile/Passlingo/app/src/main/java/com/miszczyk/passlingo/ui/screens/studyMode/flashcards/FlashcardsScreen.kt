package com.miszczyk.passlingo.ui.screens.studyMode.flashcards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.ui.components.ScreenHeader
import com.miszczyk.passlingo.ui.components.ThemedDivider
import com.miszczyk.passlingo.ui.screens.studyMode.components.BreatherScreen
import com.miszczyk.passlingo.ui.screens.studyMode.components.LoadingScreen
import com.miszczyk.passlingo.ui.screens.studyMode.components.SessionSummarySection
import com.miszczyk.passlingo.ui.screens.studyMode.flashcards.components.BackButtons
import com.miszczyk.passlingo.ui.screens.studyMode.flashcards.components.BackCard
import com.miszczyk.passlingo.ui.screens.studyMode.flashcards.components.FrontButton
import com.miszczyk.passlingo.ui.screens.studyMode.flashcards.components.FrontCard
import com.miszczyk.passlingo.ui.screens.studyMode.flashcards.viewmodel.FlashcardsViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge

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