package com.miszczyk.passlingo.ui.screens.home.util

import android.app.AppOpsManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Process
import android.provider.Settings
import androidx.core.net.toUri
import com.miszczyk.passlingo.R

fun hasUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}

fun requestUsageStatsPermission(context: Context, onError: (String) -> Unit) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
        data = "package:${context.packageName}".toUri()
    }
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        try {
            context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        } catch (_: ActivityNotFoundException) {
            onError(context.getString(R.string.toast_cannot_open_settings))
        }
    }
}