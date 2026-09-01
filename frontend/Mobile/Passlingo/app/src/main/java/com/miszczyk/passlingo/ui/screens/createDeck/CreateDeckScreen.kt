package com.miszczyk.passlingo.ui.screens.createDeck

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.ui.screens.createDeck.components.AddCardSection
import com.miszczyk.passlingo.ui.screens.createDeck.components.CardsContainer
import com.miszczyk.passlingo.ui.screens.createDeck.components.CreateDeckBottomBar
import com.miszczyk.passlingo.ui.screens.createDeck.components.DeckDetailsSection
import com.miszczyk.passlingo.ui.screens.createDeck.components.DeckStatusDialogs
import com.miszczyk.passlingo.ui.screens.createDeck.components.Header
import com.miszczyk.passlingo.ui.components.HorizontalDivider
import com.miszczyk.passlingo.ui.screens.createDeck.components.IconSelectBottomSheet
import com.miszczyk.passlingo.ui.screens.createDeck.viewmodel.CreateDeckViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDeckScreen(modifier: Modifier = Modifier, onBack: () -> Unit, viewModel: CreateDeckViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

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
            DeckDetailsSection(state = uiState.deckName, onSelectIconClicked = {viewModel.onSelectIconClicked()}, icon = uiState.deckIcon)

            Spacer(modifier = Modifier.height(spaceExtraHuge))

            HorizontalDivider(MaterialTheme.colorScheme.onSecondary)

            Spacer(modifier = Modifier.height(spaceHuge))
            AddCardSection(addedCards = uiState.addedCards, stateFront = viewModel.frontCardState, stateBack = viewModel.backCardState , onAddToDeckClicked = {viewModel.onAddToDeckClicked(addedCards = uiState.addedCards)})

            Spacer(modifier = Modifier.height(spaceExtraHuge))
            CardsContainer(addedCards = uiState.addedCards)
            Spacer(modifier = Modifier.height(spaceHuge))
        }

        HorizontalDivider(MaterialTheme.colorScheme.onSecondary)
        Spacer(modifier = Modifier.height(spaceExtraLarge))
        CreateDeckBottomBar(deckName = uiState.deckName.text.toString(), addedCards = uiState.addedCards, onSaveDeckClicked = {viewModel.onSaveDeckClicked(uiState.deckName.text.toString() ,addedCards = uiState.addedCards)})
        Spacer(modifier = Modifier.height(spaceLarge))
    }

    if(uiState.showBottomSheet){
        IconSelectBottomSheet(
            sheetState = sheetState,
            currentIcon = uiState.deckIcon,
            onIconClicked = { iconId -> viewModel.onIconClicked(iconId) },
            onDismissRequest = {viewModel.onSheetDismissed()},
            deckIconsList = viewModel.deckIconsList,
        )
    }

    DeckStatusDialogs(
        dialogState = uiState.dialogState,
        createDeckViewModel = viewModel
    )
}