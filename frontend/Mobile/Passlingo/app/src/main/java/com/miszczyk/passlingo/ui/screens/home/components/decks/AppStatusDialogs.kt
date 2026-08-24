package com.miszczyk.passlingo.ui.screens.home.components.decks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.ui.screens.home.datastore.COST_TIME
import com.miszczyk.passlingo.ui.screens.home.model.DialogItem
import com.miszczyk.passlingo.ui.screens.home.util.convertTimeToString
import com.miszczyk.passlingo.ui.screens.home.util.formatDuration
import com.miszczyk.passlingo.ui.theme.vagRoundedBlack
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun LockAppDialog(onConfirm: () -> Unit, onCancel: () -> Unit) {
    DialogComponent(
        dialog = DialogItem(
            title = "Are you sure?",
            message = "Locked apps will be unavailable until you correctly study a specific number of flashcards. This cannot be bypassed!",
            onConfirm = onConfirm,
            onConfirmText = "Lock",
            onCancel = onCancel,
            onCancelText = "Cancel",
        )
    )
}

@Composable
fun UnlockAppDialog(onConfirm: () -> Unit, onCancel: () -> Unit) {
    DialogComponent(
        dialog = DialogItem(
            title = "Unlock app?",
            message = "Unlocking this app requires you to pay with you earned study time.",
            onConfirm = onConfirm,
            onConfirmText = "Pay & Unlock",
            onCancel = onCancel,
            onCancelText = "Cancel",
            extraContent = { TimerBlock("COST TO UNLOCK", formatDuration(COST_TIME)) }
        )
    )
}

@Composable
fun InsufficientTimeDialog(onCancel: () -> Unit) {
    DialogComponent(
        dialog = DialogItem(
            title = "Not enough time",
            message = "You don't have enough earned study time to unlock this app.",
            onConfirmText = "Keep studying",
            onCancel = onCancel,
            onConfirm = onCancel,
            extraContent = { TimerBlock("REQUIRES", formatDuration(COST_TIME)) }
        )
    )
}


@Composable
private fun TimerBlock(title: String, time: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(16.dp))
            .background(
                color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontFamily = vagRoundedLight,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.5.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = convertTimeToString(time, 25.sp, 20.sp),
            textAlign = TextAlign.Center,
            fontFamily = vagRoundedBlack,
        )
    }
}