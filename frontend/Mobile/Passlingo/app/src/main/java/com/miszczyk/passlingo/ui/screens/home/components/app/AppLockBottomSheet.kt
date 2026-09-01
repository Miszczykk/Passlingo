package com.miszczyk.passlingo.ui.screens.home.components.app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.BottomSheetHeader
import com.miszczyk.passlingo.ui.components.HorizontalDivider
import com.miszczyk.passlingo.ui.screens.home.model.AppItem
import com.miszczyk.passlingo.ui.screens.home.model.AppRowState
import com.miszczyk.passlingo.ui.util.earnedTimeFor
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMediumLarge
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppLockBottomSheet(
    sheetState: SheetState,
    hasUsagePermission: Boolean,
    isLoadingApps: Boolean,
    userApps: List<AppItem>,
    selectedApps: Set<String>,
    lockedApps: Set<String>,
    onAppToggled: (String) -> Unit,
    onLockClicked: () -> Unit,
    onRequestPermission: () -> Unit,
    onDismissRequest: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val coroutineScope = rememberCoroutineScope()

            BottomSheetHeader(stringResource(R.string.label_app_lock)) {
                coroutineScope.launch {
                    sheetState.hide()
                    onDismissRequest()
                }
            }

            Spacer(modifier = Modifier.height(spaceMediumLarge))

            Text(
                stringResource(R.string.prompt_app_lock_description),
                fontSize = body,
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = vagRoundedLight,
                modifier = Modifier.padding(horizontal = spaceExtraLarge)
            )

            Spacer(modifier = Modifier.height(spaceExtraLarge))

            HorizontalDivider(MaterialTheme.colorScheme.onSecondary)

            Spacer(modifier = Modifier.height(spaceExtraLarge))

            if (!hasUsagePermission) {
                PermissionRequiredContent(onRequestPermission)
            } else if (isLoadingApps) {
                LoadingAppsContent()
            } else {
                AppListContent(userApps, selectedApps, lockedApps, onAppToggled, onLockClicked)
            }
        }
    }
}

@Composable
private fun PermissionRequiredContent(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.prompt_usage_access_required), fontSize = titleMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = vagRoundedBold
        )
    }

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spaceDefault),
        shape = RoundedCornerShape(cornerRadiusDefault),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        onClick = onRequestPermission
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spaceDefault),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Accessibility,
                contentDescription = stringResource(R.string.content_desc_accessibility),
                tint = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.width(spaceMedium))

            Text(
                text = stringResource(R.string.action_grant_permission), fontSize = titleLarge,
                color = MaterialTheme.colorScheme.background,
                fontFamily = vagRoundedBold
            )
        }
    }
}

@Composable
private fun LoadingAppsContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ColumnScope.AppListContent(
    userApps: List<AppItem>, selectedApps: Set<String>,
    lockedApps: Set<String>,
    onAppToggled: (String) -> Unit,
    onLockClicked: () -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(spaceLarge),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge)
            .weight(1f)
    ) {
        items(items = userApps, key = { it.packageName }) { app ->
            val rowState = when {
                lockedApps.contains(app.packageName) -> AppRowState.Locked
                selectedApps.contains(app.packageName) -> AppRowState.Selected
                else -> AppRowState.Normal
            }
            AppListItem(
                app = app,
                appRowState = rowState,
                onClick = {
                    onAppToggled(app.packageName)
                }
            )
        }
    }

    val buttonColor by animateColorAsState(
        targetValue = if (selectedApps.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
        label = "buttonColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (selectedApps.isNotEmpty()) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSecondary,
        label = "buttonColor"
    )

    val textDescription =
        if (selectedApps.isNotEmpty()) stringResource(
            R.string.action_lock_selected,
            (earnedTimeFor(selectedApps.size) / 60).toInt()
        ) else stringResource(R.string.prompt_select_apps_to_lock)

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spaceDefault)
            .padding(horizontal = spaceExtraLarge),
        shape = RoundedCornerShape(cornerRadiusDefault),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        onClick = {
            if (selectedApps.isNotEmpty()) onLockClicked()
        }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spaceDefault),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = stringResource(R.string.content_desc_lock),
                tint = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.width(spaceMedium))

            Text(
                text = textDescription, fontSize = titleLarge,
                color = textColor,
                fontFamily = vagRoundedBold
            )
        }
    }
}