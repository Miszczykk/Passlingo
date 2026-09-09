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
import com.miszczyk.passlingo.ui.components.buildAppNameLogo
import com.miszczyk.passlingo.ui.screens.home.components.BalanceBox
import com.miszczyk.passlingo.ui.screens.home.components.CreateBox
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckOptionsBottomSheet
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckBoxHeader
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckItem
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckStatusDialogs
import com.miszczyk.passlingo.ui.screens.home.components.decks.StudyModeBottomSheet
import com.miszczyk.passlingo.ui.screens.home.components.decks.StudySettingsBottomSheet
import com.miszczyk.passlingo.ui.screens.home.components.decks.WithoutDecks
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckBottomSheetState
import com.miszczyk.passlingo.ui.screens.home.model.deck.HasDeckName
import com.miszczyk.passlingo.ui.screens.home.viewmodel.app.AppViewModel
import com.miszczyk.passlingo.ui.screens.home.viewmodel.deck.DeckViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.PasslingoTheme
import com.miszczyk.passlingo.ui.theme.TextSize.displayLarge
import com.miszczyk.passlingo.ui.theme.TextSize.displaySmall
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.util.DeckIcons

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(value = Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCreateDeckClicked: () -> Unit,
    onEditDeckClicked: (String) -> Unit,
    appViewModel: AppViewModel = viewModel(),
    deckViewModel: DeckViewModel = viewModel()
) {
    val appUiState by appViewModel.appUiState.collectAsState()
    val deckUiState by deckViewModel.deckUiState.collectAsState()

    val sheetStateToOptions = rememberModalBottomSheetState()
    val sheetStateToStudyMode = rememberModalBottomSheetState()
    val sheetStateToStudySettings = rememberModalBottomSheetState()

    LazyColumn(
        modifier = modifier.padding(horizontal = spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = buildAppNameLogo(displaySmall, displayLarge),
                textAlign = TextAlign.Center,
                fontFamily = vagRoundedBold
            )
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
                    icon = DeckIcons.findIconFromId(deckWithCards.deck.iconResId).resId,
                    nameDeck = deckWithCards.deck.name,
                    flashcardCount = deckWithCards.flashcards.size,
                    isSelected = deckUiState.selectedDeckId == deckWithCards.deck.id,
                    onClick = { deckViewModel.selectDeck(deckWithCards.deck.id) })

                Spacer(modifier = Modifier.height(height = spaceExtraLarge))
            }
        }
    }

        when(val state = deckUiState.deckBottomSheetState){
            is DeckBottomSheetState.None -> {}

            is DeckBottomSheetState.DeckOptions -> {
                DeckOptionsBottomSheet(
                    sheetState = sheetStateToOptions,
                    deckIcon = DeckIcons.findIconFromId(state.iconResId).resId,
                    deckName = state.deckName,
                    flashcardCount = state.flashcardCount,
                    onDismissRequest = { deckViewModel.hideBottomSheet() },
                    onStudyClicked = { deckViewModel.onStudyModeClicked() },
                    onEditClicked = { deckUiState.selectedDeckId?.let { onEditDeckClicked(it) } },
                    onDeleteClicked = { deckViewModel.deleteDeck() }
                )
            }

            is DeckBottomSheetState.StudyMode -> {
                StudyModeBottomSheet(
                    sheetState = sheetStateToStudyMode,
                    deckName = state.deckName,
                    onDismissRequest = { deckViewModel.hideBottomSheet() },
                    onFlashcardClicked = {deckViewModel.onStudySettingsClicked()},
                    onQuizClicked = {deckViewModel.onStudySettingsClicked()},
                    onTypingClicked = {deckViewModel.onStudySettingsClicked()}
                )
            }

            is DeckBottomSheetState.StudySettings -> {
                StudySettingsBottomSheet(
                    sheetState = sheetStateToStudySettings,
                    onDismissRequest = {deckViewModel.hideBottomSheet()},
                    onStartSessionClicked = {}
                )
            }
        }

    DeckStatusDialogs(
        deckDialogState = deckUiState.deckDialogState,
        deckViewModel = deckViewModel,
        deckName = (deckUiState.deckDialogState as? HasDeckName)?.deckName ?: ""
    )

}

@RequiresApi(value = Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    PasslingoTheme {
        HomeScreen(onCreateDeckClicked = {}, onEditDeckClicked = {})
    }
}