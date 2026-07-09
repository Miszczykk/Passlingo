package com.miszczyk.passlingo.ui.screens.home.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "passlingo_settings")
val BLOCKED_APPS_KEY = stringSetPreferencesKey("blocked_apps")
val BALANCE_TIME_KEY = longPreferencesKey("balance_time")


const val COST_TIME : Long = 3600L