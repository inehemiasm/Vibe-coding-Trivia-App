package com.neo.trivia.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hub
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.neo.trivia.R
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.ui.common.WebViewScreen
import com.neo.trivia.ui.favorites.DevHubScreen
import com.neo.trivia.ui.favorites.MediumPostDetailScreen
import com.neo.trivia.ui.settings.SettingsScreen
import com.neo.trivia.ui.stats.StatisticsScreen
import com.neo.trivia.ui.trivia.CategorySelectionScreen
import com.neo.trivia.ui.trivia.CategoryViewModel
import com.neo.trivia.ui.trivia.QuestionScreen
import com.neo.trivia.ui.trivia.QuestionViewModel
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
data class QuestionScreen(val categoryId: Int, val difficulty: String)

@Serializable
object QuizResultScreen

@Serializable
data class MediumPostDetailScreen(val postId: String)

@Serializable
data class WebViewScreen(val url: String, val title: String)

@Serializable
sealed class Screen(
    val route: String,
    val titleRes: Int,
) {
    @Serializable
    data object Home : Screen("home", R.string.nav_home)

    @Serializable
    data object DevHub : Screen("dev_hub", R.string.nav_dev_hub)

    @Serializable
    data object Stats : Screen("stats", R.string.nav_stats)

    @Serializable
    data object Settings : Screen("settings", R.string.nav_settings)

    @Serializable
    data class QuizResultDetailScreen(val quizResultId: String) : Screen("quiz_result_detail", R.string.quiz_results_top_bar_title)
}

val Screen.icon: ImageVector
    get() =
        when (this) {
            Screen.Home -> Icons.Default.Home
            Screen.DevHub -> Icons.Default.Hub
            Screen.Stats -> Icons.Default.Star
            Screen.Settings -> Icons.Default.Settings
            else -> Icons.Default.Home
        }

val items =
    listOf(
        Screen.Home,
        Screen.DevHub,
        Screen.Stats,
        Screen.Settings,
    )

@Composable
fun NavigationApp(navController: NavHostController = rememberNavController()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.titleRes)) },
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
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<Screen.Home> { backStackEntry ->
                val triviaGraphBackStackEntry =
                    remember(backStackEntry) {
                        navController.getBackStackEntry(Screen.Home)
                    }
                val viewModel: CategoryViewModel = hiltViewModel(triviaGraphBackStackEntry)

                CategorySelectionScreen(
                    viewModel = viewModel,
                    navController = navController,
                )
            }

            composable<QuestionScreen> { backStackEntry ->
                val questionScreen: QuestionScreen = backStackEntry.toRoute()
                val triviaGraphBackStackEntry =
                    remember(backStackEntry) {
                        navController.getBackStackEntry(Screen.Home)
                    }
                val categoryViewModel: CategoryViewModel = hiltViewModel(triviaGraphBackStackEntry)
                val questionViewModel: QuestionViewModel = hiltViewModel(triviaGraphBackStackEntry)

                val state by categoryViewModel.uiState.collectAsStateWithLifecycle()
                val category = state.categories.find { it.id == questionScreen.categoryId }

                if (category != null) {
                    QuestionScreen(
                        viewModel = questionViewModel,
                        navController = navController,
                        difficulty = Difficulty.valueOf(questionScreen.difficulty),
                        category = category,
                        onQuizFinished = { _, _ ->
                            navController.navigate(QuizResultScreen) {
                                // Pop the question screen from the backstack
                                popUpTo<QuestionScreen> { inclusive = true }
                            }
                        },
                    )
                }
            }

            composable<QuizResultScreen> { backStackEntry ->
                QuizResultScreen(
                    quizResultId = null,
                    navController = navController,
                )
            }

            composable<Screen.DevHub> {
                DevHubScreen(navController = navController)
            }

            composable<MediumPostDetailScreen> { backStackEntry ->
                val detail: MediumPostDetailScreen = backStackEntry.toRoute()
                MediumPostDetailScreen(
                    postId = detail.postId,
                    navController = navController
                )
            }

            composable<WebViewScreen> { backStackEntry ->
                val webView: WebViewScreen = backStackEntry.toRoute()
                WebViewScreen(
                    url = webView.url,
                    title = webView.title,
                    navController = navController
                )
            }

            composable<Screen.Stats> {
                StatisticsScreen(navController = navController)
            }
            composable<Screen.Settings> {
                SettingsScreen(navController = navController)
            }
            composable<Screen.QuizResultDetailScreen> { backStackEntry ->
                val detail: Screen.QuizResultDetailScreen = backStackEntry.toRoute()
                QuizResultScreen(
                    quizResultId = detail.quizResultId,
                    navController = navController,
                )
            }
        }
    }
}
