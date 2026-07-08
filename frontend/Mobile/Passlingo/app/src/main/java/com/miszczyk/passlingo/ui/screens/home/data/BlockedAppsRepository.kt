package com.miszczyk.passlingo.ui.screens.home.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.miszczyk.passlingo.ui.screens.home.BLOCKED_APPS_KEY
import com.miszczyk.passlingo.ui.screens.home.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BlockedAppsRepository(private val context: Context) {

    val blockedApps: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[BLOCKED_APPS_KEY] ?: emptySet()
    }

    suspend fun setBlockedApps(packageNames: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[BLOCKED_APPS_KEY] = packageNames
        }
    }

    suspend fun addBlockedApps(packageNames: Set<String>) {
        context.dataStore.edit { prefs ->
            val current = prefs[BLOCKED_APPS_KEY] ?: emptySet()
            prefs[BLOCKED_APPS_KEY] = current + packageNames
        }
    }
}