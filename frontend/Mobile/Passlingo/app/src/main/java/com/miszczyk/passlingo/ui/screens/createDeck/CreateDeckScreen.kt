package com.miszczyk.passlingo.ui.screens.createDeck

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.HorizontalDivider
import com.miszczyk.passlingo.ui.screens.createDeck.components.AddCardSection
import com.miszczyk.passlingo.ui.screens.createDeck.components.CreateDeckBottomBar
import com.miszczyk.passlingo.ui.screens.createDeck.components.DeckDetailsSection
import com.miszczyk.passlingo.ui.screens.createDeck.components.DeckStatusDialogs
import com.miszczyk.passlingo.ui.screens.createDeck.components.FlashcardItem
import com.miszczyk.passlingo.ui.screens.createDeck.components.Header
import com.miszczyk.passlingo.ui.screens.createDeck.components.IconSelectBottomSheet
import com.miszczyk.passlingo.ui.screens.createDeck.viewmodel.CreateDeckViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.bodySmall
import com.miszczyk.passlingo.ui.theme.vagRoundedLight
import com.miszczyk.passlingo.ui.util.DeckIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDeckScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: CreateDeckViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.navigateBack.collect {
            onBack()
        }
    }

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(spaceLarge))
        Header(onClick = { viewModel.onBackClicked() })
        Spacer(modifier = Modifier.height(spaceExtraLarge))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = spaceExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                DeckDetailsSection(
                    state = viewModel.deckName,
                    onSelectIconClicked = { viewModel.onSelectIconClicked() },
                    icon = uiState.deckIcon
                )
                Spacer(modifier = Modifier.height(spaceExtraHuge))
                HorizontalDivider(MaterialTheme.colorScheme.onSecondary)
                Spacer(modifier = Modifier.height(spaceHuge))
                AddCardSection(
                    addedCards = uiState.cards.size,
                    stateFront = viewModel.frontCreateCardState,
                    stateBack = viewModel.backCreateCardState,
                    onAddToDeckClicked = { viewModel.onAddToDeckClicked() })

                Spacer(modifier = Modifier.height(spaceExtraHuge))
            }

            if (uiState.cards.isNotEmpty()) {
                items(items = uiState.cards, key = { it.id }) { card ->
                    FlashcardItem(frontText = card.front, backText = card.back, onEditClicked = { viewModel.onEditCardClicked(card) }, onDeleteClicked = { viewModel.onDeleteCardClicked(card) })
                    Spacer(modifier = Modifier.height(spaceLarge))
                }
            } else {
                item {
                    Text(
                        text = stringResource(R.string.prompt_no_cards_added),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = vagRoundedLight,
                        fontSize = bodySmall,
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(spaceHuge))
            }
        }

        HorizontalDivider(MaterialTheme.colorScheme.onSecondary)
        Spacer(modifier = Modifier.height(spaceExtraLarge))
        CreateDeckBottomBar(
            deckName = viewModel.deckName.text.toString(),
            addedCards = uiState.cards.size,
            onSaveDeckClicked = { viewModel.onSaveDeckClicked() })
        Spacer(modifier = Modifier.height(spaceLarge))
    }

    if (uiState.showBottomSheet) {
        IconSelectBottomSheet(
            sheetState = sheetState,
            currentIcon = uiState.deckIcon,
            onIconClicked = { iconId -> viewModel.onIconClicked(iconId) },
            onDismissRequest = { viewModel.onSheetDismissed() },
            deckIconsList = DeckIcons.all,
        )
    }

    DeckStatusDialogs(
        dialogState = uiState.dialogState,
        createDeckViewModel = viewModel
    )
}