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
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.aiDataStore: DataStore<Preferences> by preferencesDataStore(name = "ai_preferences")

@Singleton
class AiPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val IS_QUOTA_EXCEEDED = booleanPreferencesKey("is_quota_exceeded")
        private val QUOTA_RESET_TIME = longPreferencesKey("quota_reset_time")
        
        // Default reset interval if not specified (24 hours)
        private const val DEFAULT_RESET_INTERVAL_MS = 24 * 60 * 60 * 1000L
    }

    val isQuotaExceeded: Flow<Boolean> = context.aiDataStore.data.map { preferences ->
        val exceeded = preferences[IS_QUOTA_EXCEEDED] ?: false
        val resetTime = preferences[QUOTA_RESET_TIME] ?: 0L
        
        if (exceeded && System.currentTimeMillis() >= resetTime) {
            // Quota should be reset by now
            false
        } else {
            exceeded
        }
    }

    suspend fun setQuotaExceeded(exceeded: Boolean, retryAfterMs: Long? = null) {
        context.aiDataStore.edit { preferences ->
            preferences[IS_QUOTA_EXCEEDED] = exceeded
            if (exceeded) {
                val resetTime = System.currentTimeMillis() + (retryAfterMs ?: DEFAULT_RESET_INTERVAL_MS)
                preferences[QUOTA_RESET_TIME] = resetTime
            } else {
                preferences[QUOTA_RESET_TIME] = 0L
            }
        }
    }
    
    suspend fun checkAndResetQuota() {
        context.aiDataStore.edit { preferences ->
            val exceeded = preferences[IS_QUOTA_EXCEEDED] ?: false
            val resetTime = preferences[QUOTA_RESET_TIME] ?: 0L
            
            if (exceeded && System.currentTimeMillis() >= resetTime) {
                preferences[IS_QUOTA_EXCEEDED] = false
                preferences[QUOTA_RESET_TIME] = 0L
            }
        }
    }
}
