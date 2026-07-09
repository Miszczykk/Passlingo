package com.miszczyk.passlingo.ui.screens.home.components.decks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.ui.screens.home.util.requestUsageStatsPermission
import com.miszczyk.passlingo.ui.screens.home.viewmodel.HomeViewModel
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecksBox(viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_RESUME){
                viewModel.onReturnedFromSettings()
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }


    val animatedScaleMultiplier by animateFloatAsState(
        targetValue = if (uiState.showBottomSheet) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 600),
        label = "ScaleAnimation"
    )
    val animatedColorLock by animateColorAsState(
        targetValue = if (uiState.showBottomSheet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary,
        animationSpec = tween(durationMillis = 600),
        label = "ColorAnimation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Earn from Decks",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 25.sp,
            fontFamily = vagRoundedBold,
        )

        IconButton(
            onClick = { viewModel.onLockIconClicked() },
            modifier = Modifier
                .padding(15.dp)
                .background(
                    MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Block",
                tint = animatedColorLock,
                modifier = Modifier.scale(animatedScaleMultiplier)
            )
        }
    }

    if (uiState.showBottomSheet) {
        AppLockBottomSheet(
            sheetState = sheetState,
            hasUsagePermission = uiState.hasUsagePermission,
            userApps = uiState.userApps,
            selectedApps = uiState.selectedApps,
            blockedApps = uiState.blockedApps,
            onBlockedAppsClicked = {viewModel.onBlockedAppsClicked()},
            onAppToggled = { viewModel.onAppToggled(it) },
            onBlockClicked = { viewModel.onBlockSelectedClicked() },
            onRequestPermission = { requestUsageStatsPermission(context) },
            onDismissRequest = { viewModel.onSheetDismissed() }
        )
    }

    if (uiState.showAlertDialog) {
        BlockConfirmationDialog(
            cancelClicked = { viewModel.onDialogCancelled() },
            acceptClicked = { viewModel.onDialogConfirmed() }
        )
    }

    if(uiState.showAlertUnblockDialog){
        BlockUnblockConfirmationDialog(
            cancelClicked = {viewModel.onUnblockDialogCancelled()},
            acceptClicked = {viewModel.onUnblockDialogConfirmed()}
        )
    }
}