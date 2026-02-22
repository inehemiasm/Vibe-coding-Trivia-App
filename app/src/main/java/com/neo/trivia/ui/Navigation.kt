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
import com.neo.trivia.ui.stats.StatisticsScreen
import com.neo.trivia.ui.trivia.TriviaScreen

@Composable
fun NavigationApp(
    navController: NavHostController = androidx.navigation.compose.rememberNavController()
) {
    var currentScreen by remember { mutableStateOf("trivia") }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "trivia",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("trivia") {
                TriviaScreen()
                currentScreen = "trivia"
            }
            composable("favorites") {
                FavoritesScreen()
                currentScreen = "favorites"
            }
            composable("stats") {
                StatisticsScreen()
                currentScreen = "stats"
            }
        }
    }
}