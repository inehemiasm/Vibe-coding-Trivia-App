package com.neo.trivia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.neo.trivia.data.ThemePreferencesData
import com.neo.trivia.data.preferences.SyncPreferencesManager
import com.neo.trivia.data.preferences.ThemePreferencesManager
import com.neo.trivia.data.remote.SyncScheduler
import com.neo.trivia.ui.NavigationApp
import com.neo.trivia.ui.theme.ThemeMode
import com.neo.trivia.ui.theme.TriviaAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var syncScheduler: SyncScheduler

    @Inject
    lateinit var syncPreferencesManager: SyncPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themePreferencesManager = ThemePreferencesManager(this)

        setContent {
            val themeData by themePreferencesManager.getThemePreferences().collectAsState(
                initial = ThemePreferencesData(ThemeMode.Vibrant, false)
            )

            val isAutoSyncEnabled by syncPreferencesManager.isAutoSyncEnabled().collectAsState(initial = true)

            LaunchedEffect(isAutoSyncEnabled) {
                if (isAutoSyncEnabled) {
                    syncScheduler.scheduleSync()
                } else {
                    syncScheduler.cancelSync()
                }
            }

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
