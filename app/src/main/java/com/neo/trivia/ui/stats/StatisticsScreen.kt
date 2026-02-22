package com.neo.trivia.ui.stats

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val totalQuestions by viewModel.totalQuestions.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Home, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Statistics Cards
            StatCard(
                icon = Icons.Default.Info,
                title = "Total Questions",
                value = "$totalQuestions",
                color = MaterialTheme.colorScheme.primary
            )

            StatCard(
                icon = Icons.Default.Favorite,
                title = "Favorite Questions",
                value = "$totalQuestions",
                color = MaterialTheme.colorScheme.error
            )

            // Usage Information
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Statistics Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total Questions: $totalQuestions",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Questions are cached locally for offline access.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "You can clear your history at any time.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

class TriviaRepositoryImplMock : TriviaRepository {
    override suspend fun getQuestions(amount: Int, category: com.neo.trivia.domain.model.Category?): Result<List<Question>> {
        return Result.success(emptyList())
    }

    override fun getQuestionsFromCache(): Flow<List<Question>> {
        return flowOf(emptyList())
    }

    override fun searchQuestions(query: String): Flow<List<Question>> {
        return flowOf(emptyList())
    }

    override suspend fun clearCache() {}

    override suspend fun toggleFavorite(question: Question): Boolean {
        return false
    }

    override fun getFavoriteQuestions(): Flow<List<Question>> {
        return flowOf(emptyList())
    }

    override fun getAllQuestions(): Flow<List<Question>> {
        return flowOf(emptyList())
    }
}