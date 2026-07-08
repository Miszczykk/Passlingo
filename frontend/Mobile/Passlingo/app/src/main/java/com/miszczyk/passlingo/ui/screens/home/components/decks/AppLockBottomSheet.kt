package com.miszczyk.passlingo.ui.screens.home.components.decks

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.ui.screens.home.model.AppItem
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppLockBottomSheet(
    sheetState: SheetState,
    selectedApps: Set<String>,
    onAppToggled: (String) -> Unit,
    onBlockClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        if (mode != AppOpsManager.MODE_ALLOWED) {
            context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }


    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "App Lock",
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = vagRoundedBold
                )

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .padding(15.dp)
                        .background(
                            MaterialTheme.colorScheme.onBackground,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Select apps you want to restrict access to.\nUnlocking them will require studying!",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = vagRoundedLight
            )

            Spacer(modifier = Modifier.height(30.dp))
            val lineColor = MaterialTheme.colorScheme.onSecondary
            Canvas(modifier = Modifier.fillMaxWidth()) {
                drawLine(
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = size.width, y = 0f),
                    color = lineColor,
                    strokeWidth = 1f
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            val context = LocalContext.current
            val packageManager = context.packageManager
            val usageStatsManager =
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
            val hasPermission = mode == AppOpsManager.MODE_ALLOWED
            if (!hasPermission) {
                context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }

            val userApps = remember {
                val endTime = System.currentTimeMillis()
                val startTime = endTime - (30L * 24 * 60 * 60 * 1000)
                val statsMap = usageStatsManager.queryAndAggregateUsageStats(startTime, endTime)

                val myPackageName = context.packageName

                packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                    .filter { app ->
                        (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 &&
                                app.packageName != myPackageName
                    }.map { app ->
                        val appName = app.loadLabel(packageManager).toString()
                        val icon = packageManager.getApplicationIcon(app)
                        val timeUsed = statsMap[app.packageName]?.totalTimeInForeground ?: 0L

                        AppItem(
                            name = appName,
                            packageName = app.packageName,
                            icon = icon,
                            timeInForeground = timeUsed
                        )
                    }.sortedByDescending { it.timeInForeground }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(userApps) { app ->
                    val isChecked = selectedApps.contains(app.packageName)
                    AppListItem(
                        app = app,
                        isChecked = isChecked,
                        onClick = {
                            onAppToggled(app.packageName)
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
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
                if (selectedApps.isNotEmpty()) "Block selected (${selectedApps.size})" else "Select apps to block"

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                ),
                onClick = {
                    if (selectedApps.isNotEmpty()) onBlockClicked()
                }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Block",
                        tint = MaterialTheme.colorScheme.secondary,
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = textDescription, fontSize = 25.sp,
                        color = textColor,
                        fontFamily = vagRoundedBold
                    )
                }
            }
        }
    }

}


