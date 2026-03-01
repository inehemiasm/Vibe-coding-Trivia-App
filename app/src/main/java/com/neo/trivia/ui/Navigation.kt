package com.neo.trivia.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.ui.favorites.FavoritesScreen
import com.neo.trivia.ui.settings.SettingsScreen
import com.neo.trivia.ui.stats.StatisticsScreen
import com.neo.trivia.ui.trivia.CategoriesScreenState
import com.neo.trivia.ui.trivia.CategoryViewModel
import com.neo.trivia.ui.trivia.QuestionScreen
import com.neo.trivia.ui.trivia.QuestionViewModel
import com.neo.trivia.ui.trivia.QuizResultViewModel
import com.neo.trivia.ui.trivia.TriviaScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.neo.trivia.ui.QuizResultScreen
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
data class QuestionScreen(val categoryId: Int, val difficulty: String)

@Serializable
data class TriviaScreen(val category: String? = null)


@Serializable
object QuizResultScreen


@Serializable
sealed class Screen(
    val route: String,
    val title: String,
) {
    @Serializable
    data object Home : Screen("home", "Home")

    @Serializable
    data object Favorites : Screen("favorites", "Favorites")

    @Serializable
    data object Stats : Screen("stats", "Stats")

    @Serializable
    data object Settings : Screen("settings", "Settings")
}

val Screen.icon: ImageVector
    get() = when (this) {
        Screen.Home -> Icons.Default.Home
        Screen.Favorites -> Icons.Default.Favorite
        Screen.Stats -> Icons.Default.Star
        Screen.Settings -> Icons.Default.Settings
    }


val items = listOf(
    Screen.Home,
    Screen.Favorites,
    Screen.Stats,
    Screen.Settings,
)

@Composable
fun NavigationApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen::class.qualifiedName } == true,
                        onClick = {
                            Timber.d("Navigating to ${screen::class.simpleName}")
                            navController.navigate(screen) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation<Screen.Home>(startDestination = TriviaScreen()) {
                composable<TriviaScreen> { backStackEntry ->
                    val triviaGraphBackStackEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(Screen.Home)
                    }
                    val viewModel: CategoryViewModel = hiltViewModel(triviaGraphBackStackEntry)

                    TriviaScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }

                composable<QuestionScreen> { backStackEntry ->
                    val questionScreen: QuestionScreen = backStackEntry.toRoute()
                    val triviaGraphBackStackEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(Screen.Home)
                    }
                    val categoryViewModel: CategoryViewModel = hiltViewModel(triviaGraphBackStackEntry)
                    val questionViewModel: QuestionViewModel = hiltViewModel(triviaGraphBackStackEntry)

                    val categoriesState by categoryViewModel.categoriesState.collectAsStateWithLifecycle()
                    val category = (categoriesState as? CategoriesScreenState.Success)
                        ?.categories?.find { it.id == questionScreen.categoryId }

                    if (category != null) {
                        QuestionScreen(
                            viewModel = questionViewModel,
                            navController = navController,
                            difficulty = Difficulty.valueOf(questionScreen.difficulty),
                            category = category,
                            onQuizFinished = { _, _ ->
                                // Results are saved automatically by QuizResultViewModel
                                navController.navigate(QuizResultScreen)
                            }
                        )
                    }
                }

                composable<QuizResultScreen> { backStackEntry ->
                    val quizResultViewModel: QuizResultViewModel = hiltViewModel()

                    QuizResultScreen(
                        viewModel = quizResultViewModel,
                        navController = navController
                    )
                }
            }
            composable<Screen.Favorites> {
                FavoritesScreen(navController = navController)
            }
            composable<Screen.Stats> {
                StatisticsScreen(navController = navController)
            }
            composable<Screen.Settings> {
                SettingsScreen(navController = navController)
            }
        }
    }
}