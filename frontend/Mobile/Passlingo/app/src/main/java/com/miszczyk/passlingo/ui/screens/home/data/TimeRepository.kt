package com.miszczyk.passlingo.ui.screens.home.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.miszczyk.passlingo.ui.screens.home.datastore.BALANCE_TIME_KEY
import com.miszczyk.passlingo.ui.screens.home.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimeRepository(private val context: Context) {
    val balanceTime: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[BALANCE_TIME_KEY] ?: 0L
    }

    suspend fun addTime(secondsToAdd: Long){
        context.dataStore.edit { prefs ->
            val current = prefs[BALANCE_TIME_KEY] ?: 0L
            prefs[BALANCE_TIME_KEY] = current + secondsToAdd
        }
    }

    suspend fun substractTime(secondsToSubstract: Long){
        context.dataStore.edit { prefs ->
            val current = prefs[BALANCE_TIME_KEY] ?: 0L
            val newTime = current - secondsToSubstract
            prefs[BALANCE_TIME_KEY] = if (newTime < 0) 0L else newTime
        }
    }
}