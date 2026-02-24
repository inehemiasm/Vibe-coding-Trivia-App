package com.neo.trivia.ui.trivia

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.neo.design.cards.CategoryCard
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.ui.QuestionScreen

@Composable
fun TriviaScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }
    val categoriesState by viewModel.categoriesState.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedDifficulty by remember { mutableStateOf<Difficulty?>(null) }
    val difficulties = Difficulty.entries

    when (val state = categoriesState) {
        is CategoriesScreenState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is CategoriesScreenState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Categories", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f)
                ) {
                    items(state.categories) { category ->
                        CategoryCard(
                            categoryName = category.name,
                            onCategoryClick = { selectedCategory = category },
                            border = if (selectedCategory == category) {
                                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            } else {
                                null
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text("Difficulty", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    difficulties.forEach { difficulty ->
                        Button(
                            onClick = { selectedDifficulty = difficulty },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedDifficulty == difficulty) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                        ) {
                            Text(
                                text = difficulty.name,
                                color = if (selectedDifficulty == difficulty) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        selectedCategory?.let { category ->
                            selectedDifficulty?.let { difficulty ->
                                navController.navigate(
                                    QuestionScreen(
                                        categoryId = category.id,
                                        difficulty = difficulty.name
                                    )
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedCategory != null && selectedDifficulty != null
                ) {
                    Text("Start Quiz")
                }
            }
        }

        is CategoriesScreenState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.message)
            }
        }
    }
}
