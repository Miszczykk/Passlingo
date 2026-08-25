package com.miszczyk.passlingo.ui.screens.home.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.miszczyk.passlingo.ui.screens.home.datastore.LOCKED_APPS_KEY
import com.miszczyk.passlingo.ui.screens.home.datastore.dataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LockedAppsRepository(private val context: Context) {

    val lockedApps: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[LOCKED_APPS_KEY] ?: emptySet()
    }

    suspend fun setLockedApps(packageNames: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[LOCKED_APPS_KEY] = packageNames
        }
    }

    suspend fun addLockedApps(packageNames: Set<String>) {
        context.dataStore.edit { prefs ->
            val current = prefs[LOCKED_APPS_KEY] ?: emptySet()
            prefs[LOCKED_APPS_KEY] = current + packageNames
        }
    }

    suspend fun removeSingleLockedApp(packageNames: Set<String>) {
        context.dataStore.edit { prefs ->
            val current = prefs[LOCKED_APPS_KEY] ?: emptySet()
            prefs[LOCKED_APPS_KEY] = current - packageNames
        }
    }
}