package com.neo.trivia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.neo.trivia.data.preferences.ThemePreferencesManager
import com.neo.trivia.ui.NavigationApp
import com.neo.trivia.ui.theme.ThemeMode
import com.neo.trivia.ui.theme.TriviaAppTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themePreferencesManager = ThemePreferencesManager(this)

        setContent {
            val themeData by themePreferencesManager.getThemePreferences().collectAsState(
                initial = com.neo.trivia.data.ThemePreferencesData(ThemeMode.Vibrant, false)
            )

            TriviaAppTheme(
                darkTheme = themeData.isDarkMode,
                themeMode = themeData.themeMode,
                dynamicColor = false
            ) {
                NavigationApp()
            }
        }
    }
}
