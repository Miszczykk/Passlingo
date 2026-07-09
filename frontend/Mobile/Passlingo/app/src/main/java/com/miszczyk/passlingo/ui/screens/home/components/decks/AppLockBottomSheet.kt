package com.miszczyk.passlingo.ui.screens.home.components.decks

import android.os.Build
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
import androidx.compose.material.icons.filled.Accessibility
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
    hasUsagePermission: Boolean,
    userApps: List<AppItem>,
    selectedApps: Set<String>,
    blockedApps: Set<String>,
    onBlockedAppsClicked: () -> Unit,
    onAppToggled: (String) -> Unit,
    onBlockClicked: () -> Unit,
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

            if (!hasUsagePermission) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Usage access is required to lock apps!", fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontFamily = vagRoundedBold
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = onRequestPermission
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Accessibility,
                            contentDescription = "Accessibility",
                            tint = MaterialTheme.colorScheme.secondary,
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Grant Permission", fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.background,
                            fontFamily = vagRoundedBold
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(userApps) { app ->
                        val isChecked = selectedApps.contains(app.packageName)
                        val isBlocked = blockedApps.contains(app.packageName)
                        AppListItem(
                            app = app,
                            isChecked = isChecked,
                            isBlocked = isBlocked,
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
}