package com.neo.trivia.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.syncDataStore: DataStore<Preferences> by preferencesDataStore(name = "sync_preferences")

@Singleton
class SyncPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
        private val AUTO_SYNC_ENABLED = booleanPreferencesKey("auto_sync_enabled")
        private const val SYNC_INTERVAL_MS = 7 * 24 * 60 * 60 * 1000L 
    }

    suspend fun shouldSync(): Boolean {
        val lastSync = context.syncDataStore.data.map { preferences ->
            preferences[LAST_SYNC_TIME] ?: 0L
        }.first()
        
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastSync) > SYNC_INTERVAL_MS
    }

    suspend fun updateLastSyncTime() {
        context.syncDataStore.edit { preferences ->
            preferences[LAST_SYNC_TIME] = System.currentTimeMillis()
        }
    }

    fun isAutoSyncEnabled(): Flow<Boolean> {
        return context.syncDataStore.data.map { preferences ->
            preferences[AUTO_SYNC_ENABLED] ?: true
        }
    }

    suspend fun setAutoSyncEnabled(enabled: Boolean) {
        context.syncDataStore.edit { preferences ->
            preferences[AUTO_SYNC_ENABLED] = enabled
        }
    }
}
