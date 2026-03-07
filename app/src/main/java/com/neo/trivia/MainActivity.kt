package com.neo.trivia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.neo.trivia.data.preferences.ThemePreferencesManager
import com.neo.trivia.ui.NavigationApp
import com.neo.trivia.ui.theme.TriviaAppTheme
import com.neo.trivia.ui.theme.ThemeMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themePreferencesManager = ThemePreferencesManager(this)

        // Read theme preferences from DataStore
        val themeData = runBlocking {
            try {
                val data = themePreferencesManager.getThemePreferences().first()
                Timber.tag(TAG)
                    .d("Loaded theme preferences: ${data.themeMode}, DarkMode: ${data.isDarkMode}")
                data
            } catch (e: Exception) {
                val defaultData = com.neo.trivia.data.ThemePreferencesData(ThemeMode.Vibrant, false)
                Timber.tag(TAG)
                    .d("Using default theme preferences: ${defaultData.themeMode}, DarkMode: ${defaultData.isDarkMode}")
                defaultData
            }
        }

        setContent {
            TriviaAppTheme(
                darkTheme = themeData.isDarkMode,
                themeMode = themeData.themeMode,
            ) {
                NavigationApp()
            }
        }
    }
}