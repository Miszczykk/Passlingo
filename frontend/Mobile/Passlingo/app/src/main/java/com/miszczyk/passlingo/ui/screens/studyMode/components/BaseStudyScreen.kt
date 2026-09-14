package com.miszczyk.passlingo.ui.screens.studyMode.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.miszczyk.passlingo.ui.components.ScreenHeader
import com.miszczyk.passlingo.ui.screens.studyMode.model.BaseStudyUiState
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge

@Composable
fun BaseStudyScreen(
    uiState: BaseStudyUiState,
    onBack: () -> Unit,
    onContinueBreather: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    if (uiState.isLoading) {
        StudyModeLoadingScreen()
        return
    }

    if (uiState.currentFront == null) {
        SessionSummarySection(
            cardsToPractice = uiState.cardsToPractice, onBack = onBack, modifier = modifier
        )
        return
    }

    if (uiState.isBreather) {
        BreatherScreen(
            continueLearning = onContinueBreather, onBack = onBack, modifier = modifier
        )
        return
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(height = spaceLarge))
        ScreenHeader(title = uiState.progressText, titleFontSize = titleLarge, onClick = onBack)
        Spacer(modifier = Modifier.height(height = spaceExtraHuge)
        )
        content()
    }
}