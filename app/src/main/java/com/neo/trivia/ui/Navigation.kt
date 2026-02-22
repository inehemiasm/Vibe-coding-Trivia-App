package com.neo.trivia.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.neo.trivia.ui.favorites.FavoritesScreen
import com.neo.trivia.ui.settings.SettingsScreen
import com.neo.trivia.ui.stats.StatisticsScreen
import com.neo.trivia.ui.trivia.TriviaScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.toRoute

@Composable
fun NavigationApp(
    navController: NavHostController = androidx.navigation.compose.rememberNavController()
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Home
        ) {
            composable<Home> {
                HomeScreen(
                    onNavigateToTrivia = { category ->
                        navController.navigate(Trivia(category = category))
                    },
                    onNavigateToStats = {
                        navController.navigate(Stats)
                    },
                    onNavigateToSettings = {
                        navController.navigate(Settings)
                    }
                )
            }
            composable<Trivia> {
                val args = it.toRoute<Trivia>()
                TriviaScreen(
                    viewModel = hiltViewModel(),
                    initialCategory = args.category,
                )
            }
            composable<Favorites> {
                FavoritesScreen()
            }
            composable<Stats> {
                StatisticsScreen()
            }
            composable<Settings> {
                SettingsScreen()
            }
        }
    }
}