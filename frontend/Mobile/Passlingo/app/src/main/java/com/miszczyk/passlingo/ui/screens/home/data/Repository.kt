package com.miszczyk.passlingo.ui.screens.home.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.miszczyk.passlingo.ui.screens.home.datastore.SettingsDataStore.BALANCE_TIME_KEY
import com.miszczyk.passlingo.ui.screens.home.datastore.SettingsDataStore.LOCKED_APPS_KEY
import com.miszczyk.passlingo.ui.screens.home.datastore.SettingsDataStore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repository(private val context: Context){
    val lockedApps: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[LOCKED_APPS_KEY] ?: emptySet()
    }

    val balanceTime: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[BALANCE_TIME_KEY] ?: 0L
    }

    suspend fun lockAppsAndAddCreditTime(packageNames: Set<String>, secondsEarned: Long){
        context.dataStore.edit { prefs ->
            prefs[LOCKED_APPS_KEY] = (prefs[LOCKED_APPS_KEY] ?: emptySet()) + packageNames
            prefs[BALANCE_TIME_KEY] = (prefs[BALANCE_TIME_KEY] ?: 0L) + secondsEarned
        }
    }

    suspend fun unlockAppAndSubtractCreditTime(packageName: String, secondsLost: Long){
        context.dataStore.edit { prefs ->
            val newTime = (prefs[BALANCE_TIME_KEY] ?: 0L) - secondsLost

            prefs[LOCKED_APPS_KEY] = (prefs[LOCKED_APPS_KEY] ?: emptySet()) - packageName
            prefs[BALANCE_TIME_KEY] = if (newTime < 0) 0L else newTime
        }
    }
}