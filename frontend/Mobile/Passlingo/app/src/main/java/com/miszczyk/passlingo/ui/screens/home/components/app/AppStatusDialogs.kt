package com.miszczyk.passlingo.ui.screens.home.components.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.DialogComponent
import com.miszczyk.passlingo.ui.screens.home.components.ShadowCard
import com.miszczyk.passlingo.ui.screens.home.components.TimeToCard
import com.miszczyk.passlingo.ui.screens.home.components.TitleToCard
import com.miszczyk.passlingo.ui.model.DialogItem
import com.miszczyk.passlingo.ui.screens.home.model.DialogState
import com.miszczyk.passlingo.ui.screens.home.util.Constants.COST_TIME
import com.miszczyk.passlingo.ui.screens.home.util.formatTime
import com.miszczyk.passlingo.ui.screens.home.viewmodel.DeckViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.TextSize.small
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium

@Composable
fun AppStatusDialogs(
    dialogState: DialogState,
    deckViewModel: DeckViewModel,
    appName: String
) {

    val dialogItem = when (dialogState) {
        is DialogState.None -> return
        is DialogState.ConfirmLock -> lockAppDialog()
        is DialogState.ConfirmUnlock -> unlockAppDialog(appName)
        is DialogState.InsufficientTime -> insufficientTimeDialog(appName)
        is DialogState.Error -> errorDialog(errorMessage = dialogState.message)
    }

    DialogComponent(
        dialog = dialogItem,
        onConfirm = { if(dialogState is DialogState.Error) {deckViewModel.onRetryErrorClicked()} else {deckViewModel.onDialogConfirmed()} },
        onCancel = { deckViewModel.onDialogCancelled() })
}

@Composable
private fun lockAppDialog(): DialogItem {
    return DialogItem(
        title = stringResource(R.string.dialog_title_confirm_lock),
        message = stringResource(R.string.dialog_message_confirm_lock),
        onConfirmText = stringResource(R.string.action_lock),
        onCancelText = stringResource(R.string.action_cancel),
    )
}

@Composable
private fun unlockAppDialog(appName: String): DialogItem {
    return DialogItem(
        title = stringResource(R.string.dialog_title_confirm_unlock),
        message = stringResource(R.string.dialog_message_confirm_unlock, appName),
        onConfirmText = stringResource(R.string.action_pay_and_unlock),
        onCancelText = stringResource(R.string.action_cancel),
        extraContent = {
            TimerLock(
                stringResource(R.string.label_cost_to_unlock),
                formatTime(COST_TIME, false)
            )
        }
    )
}

@Composable
private fun insufficientTimeDialog(appName: String): DialogItem {
    return DialogItem(
        title = stringResource(R.string.dialog_title_insufficient_time),
        message = stringResource(R.string.dialog_message_insufficient_time, appName),
        onConfirmText = stringResource(R.string.action_keep_studying),
        onConfirmTextColor = MaterialTheme.colorScheme.background,
        extraContent = {
            TimerLock(
                stringResource(R.string.label_requires),
                formatTime(COST_TIME, false)
            )
        }
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
private fun TimerLock(title: String, time: String) {
    ShadowCard {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TitleToCard(title, small)

            Spacer(modifier = Modifier.height(spaceExtraSmall))

            TimeToCard(time, titleLarge, titleMedium)
        }
    }
}