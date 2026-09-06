package com.miszczyk.passlingo.ui.screens.decks.editDeck

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.screens.decks.editDeck.viewmodel.EditDeckViewModel
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.components.DeckFormContent
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.components.DeckStatusDialogs
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.components.IconSelectBottomSheet
import com.miszczyk.passlingo.ui.util.DeckIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDeckScreen(
    modifier: Modifier = Modifier,
    deckId: String,
    onBack: () -> Unit,
    viewModel: EditDeckViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(key1 = deckId) {
        viewModel.loadDeckData(deckId)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.navigateBack.collect {
            onBack()
        }
    }

    DeckFormContent(
        modifier = modifier,
        headerTitle = stringResource(id = R.string.label_edit_deck) ,

        deckNameState = viewModel.deckName,
        deckIcon = uiState.deckIcon,

        frontCardState = viewModel.frontCreateCardState,
        backCardState = viewModel.backCreateCardState,

        cards = uiState.cards,

        onBackClicked = { viewModel.onBackClicked() },
        onSelectIconClicked = { viewModel.onSelectIconClicked() },
        onAddToDeckClicked = { viewModel.onAddToDeckClicked() },
        onEditCardClicked = { viewModel.onEditCardClicked(card = it) },
        onDeleteCardClicked = { viewModel.onDeleteCardClicked(card = it) },
        onSaveDeckClicked = { viewModel.onSaveDeckClicked() }
    )

    if (uiState.showBottomSheet) {
        IconSelectBottomSheet(
            sheetState = sheetState,
            currentIcon = uiState.deckIcon,
            onIconClicked = { iconId -> viewModel.onIconClicked(selectedIcon = iconId) },
            onDismissRequest = { viewModel.onSheetDismissed() },
            deckIconsList = DeckIcons.all,
        )
    }

    DeckStatusDialogs(
        dialogState = uiState.dialogState,
        editFrontState = viewModel.editFrontState,
        editBackState = viewModel.editBackState,
        onConfirm = { viewModel.onDialogConfirmed() },
        onDismiss = { viewModel.onDialogCancelled() }
    )
}