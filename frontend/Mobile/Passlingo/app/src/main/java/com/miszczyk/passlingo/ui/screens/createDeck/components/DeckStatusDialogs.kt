package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.DialogComponent
import com.miszczyk.passlingo.ui.model.DialogItem
import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckDialogState
import com.miszczyk.passlingo.ui.screens.createDeck.viewmodel.CreateDeckViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.borderDefault
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

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
        is CreateDeckDialogState.DeleteFlashcard -> deleteFlashcard(frontText = dialogState.frontText, backText = dialogState.backText)
        is CreateDeckDialogState.EditFlashcard -> editFlashcard(stateFront = createDeckViewModel.editFrontState, stateBack = createDeckViewModel.editBackState)
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

@Composable
private fun deleteFlashcard(frontText: String, backText: String): DialogItem{
    return DialogItem(
        title = "Delete flashcard?",
        message = "Are you sure you want to delete this flashcard? This action cannot be undone.",
        onConfirmText = "Delete",
        onConfirmTextColor = MaterialTheme.colorScheme.background, //temporary
        onCancelText = stringResource(R.string.action_cancel), //TODO RED BUTTON,
        extraContent = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadiusDefault))
                .background(Transparent)
                .border(
                    width = borderDefault,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(cornerRadiusDefault)
                ).padding(spaceLarge)
            ) {
                Text(
                    text = "CARD TO DELETE",
                    fontFamily = vagRoundedBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                )
                Spacer(Modifier.height(5.dp))

                Text(
                    text = frontText,
                    fontFamily = vagRoundedBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                )

                Text(
                    text = backText,
                    fontFamily = vagRoundedBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    maxLines = 1,
                )
            }
        }
    )
}
@Composable
private fun editFlashcard(stateFront: TextFieldState, stateBack: TextFieldState): DialogItem{
    return DialogItem(
        title = "Edit Flashcard",
        message = "Update the word, meaning, or cover image of your flashcard.",
        onConfirmText = "Save",
        onConfirmTextColor = MaterialTheme.colorScheme.secondary,
        onCancelText = stringResource(R.string.action_cancel),
        extraContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CardSideInput(label = stringResource(R.string.label_front_word), hintText = stringResource(R.string.prompt_front_hint), state = stateFront, colorLine = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(spaceExtraLarge))
                CardSideInput(label = stringResource(R.string.label_back_meaning),hintText = stringResource(R.string.prompt_back_hint), state = stateBack, colorLine = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(spaceExtraLarge))
            }
        },
        isWide = true
    )
}