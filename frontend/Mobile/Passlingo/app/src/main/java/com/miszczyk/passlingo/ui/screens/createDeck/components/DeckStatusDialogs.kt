package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.DialogComponent
import com.miszczyk.passlingo.ui.model.DialogItem
import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckDialogState
import com.miszczyk.passlingo.ui.screens.createDeck.viewmodel.CreateDeckViewModel

@Composable
fun DeckStatusDialogs(
    dialogState: CreateDeckDialogState,
    createDeckViewModel: CreateDeckViewModel
) {
    val dialogItem = when(dialogState){
        is CreateDeckDialogState.None -> return
        is CreateDeckDialogState.SaveDeck -> saveDeckDialog()
        is CreateDeckDialogState.DiscardChanges -> discardChangesDialog()
        is CreateDeckDialogState.Error -> errorDialog(errorMessage = dialogState.message)
    }

    DialogComponent(
        dialog = dialogItem,
        onConfirm = {createDeckViewModel.onDialogConfirmed()},
        onCancel = {createDeckViewModel.onDialogCancelled()}
    )
}

@Composable
private fun saveDeckDialog(): DialogItem{
    return DialogItem(
        title = stringResource(R.string.dialog_title_confirm_save),
        message = stringResource(R.string.dialog_message_confirm_save),
        onConfirmText = stringResource(R.string.action_confirm),
        onCancelText = stringResource(R.string.action_cancel)
    )
}

@Composable
private fun discardChangesDialog(): DialogItem{
    return DialogItem(
        title = stringResource(R.string.dialog_title_discard_changes),
        message = stringResource(R.string.dialog_message_discard_changes),
        onConfirmText = stringResource(R.string.action_confirm),
        onConfirmTextColor = MaterialTheme.colorScheme.background, //temporary
        onCancelText = stringResource(R.string.action_cancel), //TODO RED BUTTON
    )
}

@Composable
private fun errorDialog(errorMessage: String): DialogItem{
    return DialogItem(
        title = stringResource(R.string.dialog_title_error),
        message = errorMessage,
        onConfirmText = stringResource(R.string.action_close),
        onConfirmTextColor = MaterialTheme.colorScheme.background
    )
}