package com.miszczyk.passlingo.ui.screens.home.data

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.screens.home.model.app.AppItem
import com.miszczyk.passlingo.ui.screens.home.util.Constants.ONE_MONTH_MILLIS
import com.miszczyk.passlingo.ui.screens.home.util.hasUsageStatsPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppUsageProvider(
    private val context: Context
) {
    suspend fun getInstalledAppsWithUsage(): List<AppItem> = withContext(context = Dispatchers.IO) {
        if(!hasUsageStatsPermission(context)){
            throw SecurityException(context.getString(R.string.error_missing_usage_permission))
        }

        val packageManager = context.packageManager
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val endTime = System.currentTimeMillis()

        val startTime = endTime - ONE_MONTH_MILLIS
        val statsMap = usageStatsManager.queryAndAggregateUsageStats(startTime, endTime)

        if (statsMap.isNullOrEmpty()) {
            Log.w("AppUsageProvider", "Usage stats map is empty. Permission might be silently revoked or no data is available yet.")
        }

        val myPackageName = context.packageName

        return@withContext packageManager.getInstalledApplications(PackageManager.GET_META_DATA).filter { app ->
            (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 && app.packageName != myPackageName
        }.map { app ->
            val appName = app.loadLabel(packageManager).toString()
            val icon = packageManager.getApplicationIcon(app).toBitmap().asImageBitmap()
            val timeUsed = statsMap[app.packageName]?.totalTimeInForeground ?: 0L

            AppItem(
                name = appName,
                packageName = app.packageName,
                icon = icon,
                timeInForeground = timeUsed
            )
        }.sortedByDescending { it.timeInForeground }
    }
}