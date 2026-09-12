package com.miszczyk.passlingo.ui.screens.home.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.miszczyk.passlingo.ui.screens.home.datastore.SettingsDataStore.BALANCE_TIME_KEY
import com.miszczyk.passlingo.ui.screens.home.datastore.SettingsDataStore.LOCKED_APPS_KEY
import com.miszczyk.passlingo.ui.screens.home.datastore.SettingsDataStore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimeAndAppsRepository(
    private val context: Context
) {
    val lockedApps: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[LOCKED_APPS_KEY] ?: emptySet()
    }

    val balanceTime: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[BALANCE_TIME_KEY] ?: 0L
    }

    suspend fun lockAppsAndAddCreditTime(packageNames: Set<String>, secondsEarned: Long) {
        updateTimeAndApps(timeDelta = secondsEarned, appsToLock = packageNames)
    }

    suspend fun unlockAppAndSubtractCreditTime(packageName: String, secondsLost: Long) {
        updateTimeAndApps(timeDelta = -secondsLost, appsToUnlock = setOf(packageName))
    }

    suspend fun addCreditTime(secondsEarned: Long){
        updateTimeAndApps(timeDelta = secondsEarned)
    }

    private suspend fun updateTimeAndApps(
        timeDelta: Long,
        appsToLock: Set<String> = emptySet(),
        appsToUnlock: Set<String> = emptySet()
    ){
        context.dataStore.edit { prefs ->
            val currentTime = prefs[BALANCE_TIME_KEY] ?: 0L
            prefs[BALANCE_TIME_KEY] = (currentTime + timeDelta).coerceAtLeast(minimumValue = 0L)

            if (appsToLock.isNotEmpty() || appsToUnlock.isNotEmpty()) {
                val currentApps = prefs[LOCKED_APPS_KEY] ?: emptySet()
                prefs[LOCKED_APPS_KEY] = (currentApps + appsToLock) - appsToUnlock
            }
        }
    }
}