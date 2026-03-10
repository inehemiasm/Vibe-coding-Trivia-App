package com.neo.trivia.ui.trivia

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.neo.design.buttons.PrimaryButton
import com.neo.design.cards.CategoryCard
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.ui.QuestionScreen

@Composable
fun CategorySelectionScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(CategoryIntent.LoadCategories)
    }

    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedDifficulty by remember { mutableStateOf<Difficulty?>(null) }
    val difficulties = Difficulty.entries

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else if (state.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(state.error!!, color = MaterialTheme.colorScheme.error)
        }
    } else {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            Text(
                text = "Choose a Category",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
            ) {
                items(state.categories) { category ->
                    CategoryCard(
                        categoryName = category.name,
                        icon = getCategoryIcon(category.name),
                        onCategoryClick = { selectedCategory = category },
                        selected = selectedCategory == category
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Select Difficulty",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                difficulties.forEach { difficulty ->
                    Button(
                        onClick = { selectedDifficulty = difficulty },
                        modifier = Modifier.weight(1f),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    if (selectedDifficulty == difficulty) {
                                        MaterialTheme.colorScheme.secondary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                            ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = difficulty.name,
                            color =
                                if (selectedDifficulty == difficulty) {
                                    MaterialTheme.colorScheme.onSecondary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Start Quiz",
                onClick = {
                    selectedCategory?.let { category ->
                        selectedDifficulty?.let { difficulty ->
                            navController.navigate(
                                QuestionScreen(
                                    categoryId = category.id,
                                    difficulty = difficulty.name,
                                ),
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedCategory != null && selectedDifficulty != null,
            )
        }
    }
}

private fun getCategoryIcon(name: String): ImageVector {
    return when {
        name.contains("General", ignoreCase = true) -> Icons.Filled.Lightbulb
        name.contains("Book", ignoreCase = true) -> Icons.Filled.MenuBook
        name.contains("Film", ignoreCase = true) || name.contains("Movie", ignoreCase = true) -> Icons.Filled.Palette
        name.contains("Music", ignoreCase = true) -> Icons.Filled.MusicNote
        name.contains("Television", ignoreCase = true) -> Icons.Filled.Tv
        name.contains("Science", ignoreCase = true) || name.contains("Nature", ignoreCase = true) -> Icons.Filled.Science
        name.contains("Sport", ignoreCase = true) -> Icons.Filled.Sports
        name.contains("Geography", ignoreCase = true) -> Icons.Filled.Public
        name.contains("History", ignoreCase = true) -> Icons.Filled.History
        else -> Icons.Filled.Star
    }
}
