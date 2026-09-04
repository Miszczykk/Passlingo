package com.miszczyk.passlingo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.ui.screens.home.components.BalanceBox
import com.miszczyk.passlingo.ui.screens.home.components.CreateBox
import com.miszczyk.passlingo.ui.screens.home.components.Header
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckBottomSheet
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckBoxHeader
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckItem
import com.miszczyk.passlingo.ui.screens.home.components.decks.WithoutDecks
import com.miszczyk.passlingo.ui.screens.home.viewmodel.AppViewModel
import com.miszczyk.passlingo.ui.screens.home.viewmodel.HomeViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.PasslingoTheme

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(value = Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCreateDeckClicked: () -> Unit,
    appViewModel: AppViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by appViewModel.uiState.collectAsState()

    val decksState by homeViewModel.decksState.collectAsState()
    val selectedDeckId by homeViewModel.selectedDeckId.collectAsState()
    val isBottomSheetVisible by homeViewModel.isBottomSheetVisible.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LazyColumn(
        modifier = modifier.padding(horizontal = spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Header()
            Spacer(modifier = Modifier.height(height = spaceExtraLarge))
            BalanceBox(balanceTime = uiState.balanceTime)
            Spacer(modifier = Modifier.height(height = spaceHuge))
            CreateBox(onClick = onCreateDeckClicked)
            Spacer(modifier = Modifier.height(height = spaceHuge))
            DeckBoxHeader()
            Spacer(modifier = Modifier.height(height = spaceHuge))
        }
        if (decksState.isEmpty()) {
            item {
                WithoutDecks()
            }
        } else {
            items(items = decksState, key = { it.deck.id }) { deckWithCards ->
                DeckItem(
                    icon = deckWithCards.deck.iconResId,
                    nameDeck = deckWithCards.deck.name,
                    flashcardCount = deckWithCards.flashcards.size,
                    isSelected = selectedDeckId == deckWithCards.deck.id,
                    onClick = { homeViewModel.selectDeck(deckWithCards.deck.id) })

                Spacer(modifier = Modifier.height(height = spaceExtraLarge))
            }
        }
    }

    if (isBottomSheetVisible && selectedDeckId != null) {
        val selectedDeck = decksState.find { it.deck.id == selectedDeckId }

        if (selectedDeck != null) {
            DeckBottomSheet(
                sheetState = sheetState,
                deckIcon = selectedDeck.deck.iconResId,
                deckName = selectedDeck.deck.name,
                flashcardCount = selectedDeck.flashcards.size,
                onDismissRequest = {
                    homeViewModel.hideBottomSheet()
                },
                onStudyClicked = {},
                onEditClicked = {},
                onDeleteClicked = {}
            )
        }
    }
}

@RequiresApi(value = Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    PasslingoTheme {
        HomeScreen(onCreateDeckClicked = {})
    }
}