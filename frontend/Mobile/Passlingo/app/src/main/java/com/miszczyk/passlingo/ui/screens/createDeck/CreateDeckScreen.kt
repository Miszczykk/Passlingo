package com.miszczyk.passlingo.ui.screens.createDeck

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.ui.screens.createDeck.components.AddCardSection
import com.miszczyk.passlingo.ui.screens.createDeck.components.CreateDeckBottomBar
import com.miszczyk.passlingo.ui.screens.createDeck.components.DeckDetailsSection
import com.miszczyk.passlingo.ui.screens.createDeck.components.DeckStatusDialogs
import com.miszczyk.passlingo.ui.screens.createDeck.components.Header
import com.miszczyk.passlingo.ui.screens.createDeck.components.HorizontalDivider
import com.miszczyk.passlingo.ui.screens.createDeck.viewmodel.CreateDeckViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge

@Composable
fun CreateDeckScreen(modifier: Modifier = Modifier, onBack: () -> Unit, viewModel: CreateDeckViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateBack.collect {
            onBack()
        }
    }

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(spaceLarge))
        Header(onClick = {viewModel.onBackClicked()})
        Spacer(modifier = Modifier.height(spaceExtraLarge))

        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = spaceExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DeckDetailsSection(state = viewModel.deckNameState)

            Spacer(modifier = Modifier.height(spaceExtraHuge))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(spaceHuge))
            AddCardSection(addedCards = uiState.addedCards)
        }

        HorizontalDivider()
        Spacer(modifier = Modifier.height(spaceExtraLarge))
        CreateDeckBottomBar(deckName = viewModel.deckNameState.text.toString(), addedCards = uiState.addedCards, onSaveDeckClicked = {viewModel.onSaveDeckClicked(addedCards = uiState.addedCards)})
        Spacer(modifier = Modifier.height(spaceLarge))
    }

    DeckStatusDialogs(
        dialogState = uiState.dialogState,
        createDeckViewModel = viewModel
    )
}