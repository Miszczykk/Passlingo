package com.miszczyk.passlingo.ui.screens.home.data

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.miszczyk.passlingo.ui.screens.home.model.AppItem

class AppUsageProvider(private val context: Context) {
    fun getInstalledAppsWithUsage(): List<AppItem> {
        val packageManager = context.packageManager
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val endTime = System.currentTimeMillis()
        val startTime = endTime - (30L * 24 * 60 * 60 * 1000)
        val statsMap = usageStatsManager.queryAndAggregateUsageStats(startTime, endTime)

        val myPackageName = context.packageName

        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
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
}