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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecksBox() {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showAlertDialog by remember { mutableStateOf(false) }
    var selectedApps by remember { mutableStateOf(setOf<String>()) }
    val sheetState = rememberModalBottomSheetState()


    val animatedScaleMultiplier by animateFloatAsState(
        targetValue = if (showBottomSheet) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 600),
        label = "ScaleAnimation"
    )
    val animatedColorLock by animateColorAsState(
        targetValue = if (showBottomSheet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary,
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
            onClick = { showBottomSheet = true },
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

    if (showBottomSheet) {
        AppLockBottomSheet(
            sheetState = sheetState,
            selectedApps = selectedApps,
            onAppToggled = { clickedPackageName ->
                selectedApps = if (selectedApps.contains(clickedPackageName)) {
                    selectedApps - clickedPackageName
                } else {
                    selectedApps + clickedPackageName
                }
            },
            onBlockClicked = {
                if (selectedApps.isNotEmpty()) showAlertDialog = true
            },
            onDismissRequest = { showBottomSheet = false }
        )
    }

    if (showAlertDialog) {
        BlockConfirmationDialog(
            cancelClicked = {
                showBottomSheet = false
                showAlertDialog = false
            },
            {
                showBottomSheet = false
                showAlertDialog = false
            }
        )
    }
}