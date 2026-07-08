package com.miszczyk.passlingo.ui.screens.home

import android.app.AlertDialog
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.PasslingoTheme
import com.miszczyk.passlingo.ui.theme.vagRoundedBlack
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.Preferences

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "passlingo_settings")
val BLOCKED_APPS_KEY = stringSetPreferencesKey("blocked_apps")

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {


    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Header()
        Spacer(modifier = Modifier.height(30.dp))
        BalanceBox()
        Spacer(modifier = Modifier.height(40.dp))
        CreateBox()
        Spacer(modifier = Modifier.height(40.dp))
        DecksBox()
    }
}

@Composable
private fun Header() {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontSize = 55.sp)) {
            append(stringResource(R.string.app_name))
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.secondary, fontSize = 110.sp
            )
        ) {
            append('.')
        }
    }
    Text(text = annotatedText, textAlign = TextAlign.Center, fontFamily = vagRoundedBold)
}

@Composable
private fun BalanceBox(modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "AVAILABLE BALANCE",
            textAlign = TextAlign.Center,
            fontFamily = vagRoundedLight,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.5.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .shadow(elevation = 3.dp, shape = RoundedCornerShape(16.dp))
                .background(
                    color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 20.dp)
        ) {
            TimerText()
        }
    }
}

@Composable
private fun TimerText() {
    val rawTime = "01h 45m 30s"

    Text(
        text = convertTimeToString(rawTime, 80.sp, 30.sp),
        textAlign = TextAlign.Center,
        fontFamily = vagRoundedBlack,
        modifier = Modifier.offset(y = (10).dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateBox() {
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isAnimating) 0.2f else 0.15f,
        animationSpec = tween(durationMillis = 300),
        label = "AlphaAnimation"
    )

    val animatedRadiusMultiplier by animateFloatAsState(
        targetValue = if (isAnimating) 1.5f else 1.0f,
        animationSpec = tween(durationMillis = 300),
        label = "RadiusAnimation"
    )

    val animatedRotation by animateFloatAsState(
        targetValue = if (isAnimating) 45f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "RotationAnimation"
    )

    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        Button(
            onClick = {
                coroutineScope.launch {
                    isAnimating = true
                    delay(300)
                    isAnimating = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 6.dp
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                val circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = animatedAlpha)
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        color = circleColor,
                        radius = 250f * animatedRadiusMultiplier,
                        center = Offset(x = size.width, y = 0f)
                    )
                    drawCircle(
                        color = circleColor,
                        radius = 200f * animatedRadiusMultiplier,
                        center = Offset(x = 0f, y = size.height)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            )
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create flashcards",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(40.dp)
                                .rotate(animatedRotation)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Create flashcards",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 30.sp,
                        fontFamily = vagRoundedBold,
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DecksBox() {
    var showBottomSheet by remember { mutableStateOf(false) }
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

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val blockedAppsFlow: Flow<Set<String>> = remember {
        context.dataStore.data.map { preferences ->
            preferences[BLOCKED_APPS_KEY] ?: emptySet()
        }
    }
    val savedBlockedApps by blockedAppsFlow.collectAsState(initial = emptySet())

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

    var showAlertDialog by remember { mutableStateOf(false) }
    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = {
                showAlertDialog = false
            },
            title = {
                Text(
                    text = "Are you sure?", fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = vagRoundedBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Locked apps will be unavailable until you correctly study a specific number of flashcards. This cannot be bypassed!",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = vagRoundedLight,
                    textAlign = TextAlign.Center,
                )
            },
            confirmButton = @androidx.compose.runtime.Composable {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    TextButton(
                        onClick = {}, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onBackground
                        ), modifier = Modifier.fillMaxWidth().weight(1f), shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = vagRoundedBold,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }

                    Button(
                        onClick = {}, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ), modifier = Modifier.fillMaxWidth().weight(1f), shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Block",
                                tint = MaterialTheme.colorScheme.secondary,
                            )

                            Text(
                                text = "Lock",
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                fontFamily = vagRoundedBold,
                            )
                        }
                    }

                }
            }
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
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
                        onClick = { showBottomSheet = false },
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



                data class AppItem(
                    val name: String,
                    val packageName: String,
                    val icon: Drawable,
                    val timeInForeground: Long
                )

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

                var selectedApps by remember { mutableStateOf(setOf<String>()) }


                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(userApps) { app ->
                        val isChecked = selectedApps.contains(app.packageName)
                        val checkboxScale by animateFloatAsState(
                            targetValue = if (isChecked) 1.2f else 1.0f,
                            animationSpec = tween(durationMillis = 300),
                            label = "CheckboxScale"
                        )

                        val rowBackgroundColor by animateColorAsState(
                            targetValue = if (isChecked) MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.15f
                            ) else Color.Transparent,
                            animationSpec = tween(durationMillis = 300),
                            label = "RowBackground"
                        )

                        val circleColor by animateColorAsState(
                            targetValue = if (isChecked) MaterialTheme.colorScheme.secondary else Color.Transparent,
                            label = "CircleColor"
                        )
                        val borderColor by animateColorAsState(
                            targetValue = if (isChecked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground,
                            label = "BorderColor"
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(rowBackgroundColor)
                                .clickable {
                                    selectedApps = if (isChecked) {
                                        //TODO calculate available time and odd
                                        selectedApps - app.packageName
                                    } else {
                                        selectedApps + app.packageName
                                    }

                                }
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(20.dp), verticalAlignment = Alignment.CenterVertically
                        ) {
                            val bitmap = remember(app.icon) { app.icon.toBitmap().asImageBitmap() }

                            Image(
                                bitmap = bitmap,
                                contentDescription = app.name,
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))


                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                            ) {
                                Text(
                                    text = app.name,
                                    fontFamily = vagRoundedBold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                val totalSeconds = app.timeInForeground / 1000
                                val hours = totalSeconds / 3600
                                val minutes = (totalSeconds % 3600) / 60
                                val seconds = totalSeconds % 60

                                val rawTime = if (hours > 0) {
                                    String.format("%02dh %02dm %02ds", hours, minutes, seconds)
                                } else if (minutes > 0) {
                                    String.format("%02dm %02ds", minutes, seconds)
                                } else {
                                    String.format("%02ds", seconds)
                                }
                                Text(
                                    text = convertTimeToString(
                                        rawTime = rawTime,
                                        numberFont = 15.sp,
                                        textFont = 12.sp
                                    ),
                                    fontFamily = vagRoundedBold,
                                )

                            }
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .scale(checkboxScale)
                                    .border(width = 2.dp, color = borderColor, shape = CircleShape)
                                    .background(color = circleColor, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isChecked) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Checked",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
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
                        if (selectedApps.isNotEmpty()) {
                            showAlertDialog = true
//                            showBottomSheet = false
                        }
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

@Composable
private fun convertTimeToString(
    rawTime: String,
    numberFont: TextUnit,
    textFont: TextUnit
): AnnotatedString {
    val annotatedTime = buildAnnotatedString {
        rawTime.forEach { char ->
            when {
                char.isDigit() -> {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary, fontSize = numberFont
                        )
                    ) { append(char.toString()) }
                }

                char.isLetter() -> {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.secondary, fontSize = textFont
                        )
                    ) { append(char.toString()) }
                }

                else -> {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Transparent, fontSize = numberFont
                        )
                    ) { append(char.toString()) }
                }
            }
        }
    }
    return annotatedTime
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    PasslingoTheme {
        HomeScreen()
    }
}