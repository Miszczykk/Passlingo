package com.miszczyk.passlingo.ui.screens.home.components.decks

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.DialogComponent
import com.miszczyk.passlingo.ui.model.DialogItem
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckDialogState
import com.miszczyk.passlingo.ui.screens.home.viewmodel.deck.DeckViewModel

@Composable
fun DeckStatusDialogs(
    deckDialogState: DeckDialogState, deckViewModel: DeckViewModel, deckName: String
) {
    val dialogItem = when (deckDialogState) {
        is DeckDialogState.None -> return
        is DeckDialogState.ConfirmDelete -> deleteDeckDialog(deckName = deckName)
        is DeckDialogState.Error -> errorDialog(errorMessage = deckDialogState.message)
    }

    DialogComponent(dialog = dialogItem, onConfirm = {
        if (deckDialogState is DeckDialogState.Error) {
            deckViewModel.onRetryErrorClicked()
        } else {
            deckViewModel.onDialogConfirmed()
        }
    }, onCancel = { deckViewModel.onDialogCancelled() }
    )
}

@Composable
private fun deleteDeckDialog(deckName: String): DialogItem {
    return DialogItem(
        title = stringResource(id = R.string.dialog_title_delete_deck),
        message = stringResource(id = R.string.dialog_message_delete_deck),
        onConfirmText = stringResource(id = R.string.action_delete),
        onConfirmTextColor = MaterialTheme.colorScheme.background,
        onConfirmBackgroundColor = MaterialTheme.colorScheme.error,
        onCancelText = stringResource(id = R.string.action_cancel),
    )
}

@Composable
private fun errorDialog(errorMessage: String): DialogItem {
    return DialogItem(
        title = stringResource(R.string.dialog_title_error),
        message = errorMessage,
        onConfirmText = stringResource(R.string.action_close),
        onConfirmTextColor = MaterialTheme.colorScheme.background
    )
}