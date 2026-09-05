package com.miszczyk.passlingo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.ui.components.AppNameLogo
import com.miszczyk.passlingo.ui.screens.home.components.BalanceBox
import com.miszczyk.passlingo.ui.screens.home.components.CreateBox
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckBottomSheet
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckBoxHeader
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckItem
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckStatusDialogs
import com.miszczyk.passlingo.ui.screens.home.components.decks.WithoutDecks
import com.miszczyk.passlingo.ui.screens.home.viewmodel.app.AppViewModel
import com.miszczyk.passlingo.ui.screens.home.viewmodel.deck.DeckViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.PasslingoTheme
import com.miszczyk.passlingo.ui.theme.TextSize.displayLarge
import com.miszczyk.passlingo.ui.theme.TextSize.displaySmall
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(value = Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCreateDeckClicked: () -> Unit,
    appViewModel: AppViewModel = viewModel(),
    deckViewModel: DeckViewModel = viewModel()
) {
    val appUiState by appViewModel.appUiState.collectAsState()
    val deckUiState by deckViewModel.deckUiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LazyColumn(
        modifier = modifier.padding(horizontal = spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(text = AppNameLogo(displaySmall, displayLarge), textAlign = TextAlign.Center, fontFamily = vagRoundedBold)
            Spacer(modifier = Modifier.height(height = spaceExtraLarge))
            BalanceBox(balanceTime = appUiState.balanceTime)
            Spacer(modifier = Modifier.height(height = spaceHuge))
            CreateBox(onClick = onCreateDeckClicked)
            Spacer(modifier = Modifier.height(height = spaceHuge))
            DeckBoxHeader(appViewModel = appViewModel)
            Spacer(modifier = Modifier.height(height = spaceHuge))
        }
        if (deckUiState.decks.isEmpty()) {
            item {
                WithoutDecks()
            }
        } else {
            items(items = deckUiState.decks, key = { it.deck.id }) { deckWithCards ->
                DeckItem(
                    icon = deckWithCards.deck.iconResId,
                    nameDeck = deckWithCards.deck.name,
                    flashcardCount = deckWithCards.flashcards.size,
                    isSelected = deckUiState.selectedDeckId == deckWithCards.deck.id,
                    onClick = { deckViewModel.selectDeck(deckWithCards.deck.id) })

                Spacer(modifier = Modifier.height(height = spaceExtraLarge))
            }
        }
    }

    if (deckUiState.showBottomSheet && deckUiState.selectedDeckId != null) {
        val selectedDeck = deckUiState.decks.find { it.deck.id == deckUiState.selectedDeckId }

        if (selectedDeck != null) {
            DeckBottomSheet(
                sheetState = sheetState,
                deckIcon = selectedDeck.deck.iconResId,
                deckName = selectedDeck.deck.name,
                flashcardCount = selectedDeck.flashcards.size,
                onDismissRequest = {
                    deckViewModel.hideBottomSheet()
                },
                onStudyClicked = {},
                onEditClicked = {},
                onDeleteClicked = { deckViewModel.deleteDeck() }
            )
        }
    }

    DeckStatusDialogs(
        deckDialogState = deckUiState.deckDialogState,
        deckViewModel = deckViewModel,
        deckName = deckUiState.decks.find { it.deck.id == deckUiState.selectedDeckId }?.deck?.name ?: ""
    )
}

@RequiresApi(value = Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    PasslingoTheme {
        HomeScreen(onCreateDeckClicked = {})
    }
}