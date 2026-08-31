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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.screens.home.components.app.AppLockBottomSheet
import com.miszczyk.passlingo.ui.screens.home.components.app.AppStatusDialogs
import com.miszczyk.passlingo.ui.screens.home.model.HasPackageName
import com.miszczyk.passlingo.ui.screens.home.util.requestUsageStatsPermission
import com.miszczyk.passlingo.ui.screens.home.viewmodel.DeckViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMediumLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckBox(modifier: Modifier = Modifier, viewModel: DeckViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
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
            .padding(horizontal = spaceExtraLarge),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.label_earn_from_decks),
            color = MaterialTheme.colorScheme.primary,
            fontSize = titleLarge,
            fontFamily = vagRoundedBold,
        )

        IconButton(
            onClick = { viewModel.onLockIconClicked() },
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(cornerRadiusDefault)
                )
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = stringResource(R.string.content_desc_lock),
                tint = animatedColorLock,
                modifier = Modifier.scale(animatedScaleMultiplier)
            )
        }
    }

    if (uiState.showBottomSheet) {
        AppLockBottomSheet(
            sheetState = sheetState,
            hasUsagePermission = uiState.hasUsagePermission,
            isLoadingApps = uiState.isLoadingApps,
            userApps = uiState.userApps,
            selectedApps = uiState.selectedApps,
            lockedApps = uiState.lockedApps,
            onAppToggled = { viewModel.onAppToggled(it) },
            onLockClicked = { viewModel.onLockSelectedClicked() },
            onRequestPermission = {
                requestUsageStatsPermission(
                    context,
                    onError = { errorMessage -> viewModel.showPermissionError(errorMessage) })
            },
            onDismissRequest = { viewModel.onSheetDismissed() }
        )
    }

    val appName = when (val state = uiState.dialogState) {
        is HasPackageName -> uiState.userApps.find { it.packageName == state.packageName }?.name
            ?: ""
        else -> ""
    }

    AppStatusDialogs(
        dialogState = uiState.dialogState,
        deckViewModel = viewModel,
        appName = appName
    )
}