package com.neo.trivia.data.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.neo.trivia.ui.theme.ThemeMode
import com.neo.trivia.data.ThemePreferencesData

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

class ThemePreferencesManager(private val context: Context) {

    companion object {
        private val PREF_THEME_MODE = stringPreferencesKey("pref_theme_mode")
        private val PREF_DARK_MODE = stringPreferencesKey("pref_dark_mode")
    }

    fun getThemePreferences(): Flow<ThemePreferencesData> {
        return context.dataStore.data.map { preferences ->
            val themeModeName = preferences[PREF_THEME_MODE]
            val isDarkMode = preferences[PREF_DARK_MODE]?.toBoolean() ?: false

            val themeMode = if (themeModeName != null) {
                try {
                    ThemeMode.valueOf(themeModeName)
                } catch (e: IllegalArgumentException) {
                    ThemeMode.Vibrant // default fallback
                }
            } else {
                ThemeMode.Vibrant // default fallback
            }

            ThemePreferencesData(themeMode, isDarkMode)
        }
    }

    suspend fun saveThemePreferences(themeMode: ThemeMode, isDarkMode: Boolean) {
        Log.d("ThemeSwitch", "Saving theme preferences: $themeMode, DarkMode: $isDarkMode")
        context.dataStore.edit { preferences ->
            preferences[PREF_THEME_MODE] = themeMode.name
            preferences[PREF_DARK_MODE] = isDarkMode.toString()
        }
        Log.d("ThemeSwitch", "Theme preferences saved successfully")
    }
}