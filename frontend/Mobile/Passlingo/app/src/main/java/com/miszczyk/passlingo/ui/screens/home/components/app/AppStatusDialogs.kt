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
import com.miszczyk.passlingo.ui.components.ShadowCard
import com.miszczyk.passlingo.ui.components.CardTime
import com.miszczyk.passlingo.ui.components.CardTitle
import com.miszczyk.passlingo.ui.model.DialogItem
import com.miszczyk.passlingo.ui.screens.home.model.app.AppDialogState
import com.miszczyk.passlingo.ui.screens.home.util.Constants.COST_TIME_SECONDS
import com.miszczyk.passlingo.ui.util.formatTime
import com.miszczyk.passlingo.ui.screens.home.viewmodel.app.AppViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.TextSize.small
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium

@Composable
fun AppStatusDialogs(
    appDialogState: AppDialogState, appViewModel: AppViewModel, appName: String
) {
    val dialogItem = when (appDialogState) {
        is AppDialogState.None -> return
        is AppDialogState.ConfirmLock -> lockAppDialog()
        is AppDialogState.ConfirmUnlock -> unlockAppDialog(appName = appName)
        is AppDialogState.InsufficientTime -> insufficientTimeDialog(appName = appName)
        is AppDialogState.Error -> errorDialog(errorMessage = appDialogState.message)
    }

    DialogComponent(dialog = dialogItem, onConfirm = {
        if (appDialogState is AppDialogState.Error) {
            appViewModel.onRetryErrorClicked()
        } else {
            appViewModel.onDialogConfirmed()
        }
    }, onCancel = { appViewModel.onDialogCancelled() }
    )
}

@Composable
private fun lockAppDialog(): DialogItem {
    return DialogItem(
        title = stringResource(R.string.dialog_title_confirm_lock),
        message = stringResource(R.string.dialog_message_confirm_lock),
        onConfirmText = stringResource(id = R.string.action_lock),
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
                title = stringResource(R.string.label_cost_to_unlock),
                time = formatTime(totalSeconds = COST_TIME_SECONDS, forceFullFormat = false)
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
                title = stringResource(R.string.label_requires),
                time = formatTime(totalSeconds = COST_TIME_SECONDS, forceFullFormat = false)
            )
        }
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
private fun TimerLock(title: String, time: String) {
    ShadowCard {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CardTitle(titleText = title, titleFontSize = small)

            Spacer(modifier = Modifier.height(height = spaceExtraSmall))

            CardTime(timeText = time, numberFontSize = titleLarge, textFontSize = titleMedium)
        }
    }
}