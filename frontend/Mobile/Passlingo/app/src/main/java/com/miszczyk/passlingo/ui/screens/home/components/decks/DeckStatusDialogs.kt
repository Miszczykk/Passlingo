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
    deckDialogState: DeckDialogState, deckViewModel: DeckViewModel, deckName: String, onContinueSession: () -> Unit = {}
) {
    val dialogItem = when (deckDialogState) {
        is DeckDialogState.None -> return
        is DeckDialogState.ConfirmDelete -> deleteDeckDialog(deckName = deckName)
        is DeckDialogState.Error -> errorDialog(errorMessage = deckDialogState.message)
        is DeckDialogState.ResumeSession -> resumeSessionDialog()
    }

    DialogComponent(dialog = dialogItem, onConfirm = {
        when(deckDialogState){
            is DeckDialogState.Error -> deckViewModel.onRetryErrorClicked()
            is DeckDialogState.ResumeSession -> onContinueSession()
            else -> deckViewModel.onDialogConfirmed()
        }
    }, onCancel = {
        if (deckDialogState is DeckDialogState.ResumeSession) {
            deckViewModel.onContinueSessionCancelled()
        } else {
            deckViewModel.onDialogCancelled()
        }
    }
    )
}

@Composable
private fun deleteDeckDialog(deckName: String): DialogItem {
    return DialogItem(
        title = stringResource(id = R.string.dialog_title_delete_deck),
        message = stringResource(id = R.string.dialog_message_delete_deck, deckName),
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

@Composable
private fun resumeSessionDialog(): DialogItem {
    return DialogItem(
        title = "Resume Session?",
        message = "You have an unfinished study session for this deck. Would you like to pick up exactly where you left off?",
        onConfirmText = "Continue",
        onCancelText = "Start Over",
    )
}