package com.miszczyk.passlingo.ui.screens.home.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object SettingsDataStore {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "passlingo_settings", corruptionHandler = ReplaceFileCorruptionHandler { exception ->
            Log.e("SettingsDataStore", "File DataStore is corruption!", exception)
            emptyPreferences()
        }
    )
    val LOCKED_APPS_KEY = stringSetPreferencesKey("locked_apps")
    val BALANCE_TIME_KEY = longPreferencesKey("balance_time")
}