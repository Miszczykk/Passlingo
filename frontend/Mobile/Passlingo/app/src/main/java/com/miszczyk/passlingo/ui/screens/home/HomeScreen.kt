package com.miszczyk.passlingo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.ui.screens.home.components.BalanceBox
import com.miszczyk.passlingo.ui.screens.home.components.CreateBox
import com.miszczyk.passlingo.ui.screens.home.components.Header
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckBox
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckItem
import com.miszczyk.passlingo.ui.screens.home.components.decks.WithoutDecks
import com.miszczyk.passlingo.ui.screens.home.viewmodel.DeckViewModel
import com.miszczyk.passlingo.ui.screens.home.viewmodel.HomeViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.PasslingoTheme

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCreateDeckClicked: () -> Unit,
    viewModel: DeckViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val decks by homeViewModel.decksState.collectAsState()
    var selectedDeckId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier.padding(horizontal = spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Header()
            Spacer(modifier = Modifier.height(spaceExtraLarge))
            BalanceBox(balanceTime = uiState.balanceTime)
            Spacer(modifier = Modifier.height(spaceHuge))
            CreateBox(onClick = onCreateDeckClicked)
            Spacer(modifier = Modifier.height(spaceHuge))
            DeckBox()
            Spacer(modifier = Modifier.height(spaceHuge))
        }
        if (decks.isEmpty()) {
            item {
                WithoutDecks()
            }
        } else {
            items(items = decks, key = { it.deck.id }) { deckWithCards ->
                DeckItem(
                    icon = deckWithCards.deck.iconResId,
                    nameDeck = deckWithCards.deck.name,
                    numberCards = deckWithCards.flashcards.size,
                    isSelected = selectedDeckId == deckWithCards.deck.id,
                    onClick = {selectedDeckId = deckWithCards.deck.id}
                )

                Spacer(modifier = Modifier.height(spaceExtraLarge))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    PasslingoTheme {
        HomeScreen(onCreateDeckClicked = {})
    }
}