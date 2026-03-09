package com.neo.trivia.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.neo.design.cards.AppCard
import com.neo.trivia.data.preferences.ThemePreferencesManager
import com.neo.trivia.ui.theme.ThemeMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val currentContext = androidx.compose.ui.platform.LocalContext.current
    val themePreferencesManager = ThemePreferencesManager(currentContext)
    val scope = rememberCoroutineScope()

    val themeData by themePreferencesManager.getThemePreferences().collectAsState(
        initial = com.neo.trivia.data.ThemePreferencesData(ThemeMode.Vibrant, false)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            item {
                ThemeSelectionCard(
                    currentTheme = themeData.themeMode.name,
                    onThemeSelect = { selectedTheme ->
                        scope.launch {
                            themePreferencesManager.saveThemePreferences(
                                ThemeMode.valueOf(selectedTheme),
                                themeData.isDarkMode
                            )
                        }
                    },
                )
            }

            item {
                DarkModeToggle(
                    isDarkMode = themeData.isDarkMode,
                    onDarkModeToggle = {
                        scope.launch {
                            themePreferencesManager.saveThemePreferences(
                                themeData.themeMode,
                                !themeData.isDarkMode
                            )
                        }
                    },
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            item {
                AppCard(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalPadding = 16.dp,
                    verticalPadding = 16.dp,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = "Trivia App",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Text(
                            text = "Version 1.0",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "Built with Clean Architecture, Jetpack Compose, and Hilt",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeSelectionCard(
    currentTheme: String,
    onThemeSelect: (String) -> Unit,
) {
    val themes = listOf("Vibrant", "Ocean", "Sunset", "Mint")

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Select Theme",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        themes.forEach { theme ->
            ThemeOptionChip(
                text = theme,
                selected = currentTheme == theme,
                onClick = {
                    onThemeSelect(theme)
                },
            )
        }
    }
}

@Composable
fun ThemeOptionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        leadingIcon =
            if (selected) {
                {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                }
            } else {
                null
            },
    )
}

@Composable
fun DarkModeToggle(
    isDarkMode: Boolean,
    onDarkModeToggle: () -> Unit,
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        horizontalPadding = 16.dp,
        verticalPadding = 16.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Column {
                    Text(
                        text = "Dark Mode",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = if (isDarkMode) "Currently enabled" else "Enable dark theme",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Switch(
                checked = isDarkMode,
                onCheckedChange = { onDarkModeToggle() }
            )
        }
    }
}
