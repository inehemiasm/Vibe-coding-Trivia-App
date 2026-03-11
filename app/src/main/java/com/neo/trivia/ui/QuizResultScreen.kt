package com.neo.trivia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.neo.design.appbar.TriviaTopAppBar
import com.neo.design.buttons.PrimaryButton
import com.neo.trivia.R
import com.neo.trivia.ui.Components.QuizResultCard
import com.neo.trivia.ui.trivia.QuizResultIntent
import com.neo.trivia.ui.trivia.QuizResultViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultScreen(
    quizResultId: String? = null,
    viewModel: QuizResultViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(quizResultId) {
        if (quizResultId != null) {
            Timber.d("Loading quiz result by ID: $quizResultId")
            viewModel.onIntent(QuizResultIntent.LoadResultById(quizResultId))
        } else {
            Timber.d("Loading latest quiz results...")
            viewModel.onIntent(QuizResultIntent.LoadSavedResults)
        }
    }

    Scaffold(
        topBar = {
            TriviaTopAppBar(
                title = stringResource(if (quizResultId != null) R.string.quiz_details_title else R.string.quiz_results_top_bar_title),
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() },
                actions = {
                    if (state.quizResults.isNotEmpty()) {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(Icons.Default.Share, contentDescription = "Share")
                        }
                    }
                }
            )
        },
    ) { paddingValues ->
        if (state.isLoading) {
             Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                 androidx.compose.material3.CircularProgressIndicator()
             }
        } else if (state.quizResults.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ScoreCard(score = state.score, totalQuestions = state.quizResults.size)
                }

                if (quizResultId == null) {
                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.onIntent(QuizResultIntent.ResetQuiz)
                                    navController.popBackStack()
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(stringResource(R.string.quiz_retake), fontWeight = FontWeight.Bold)
                            }

                            PrimaryButton(
                                text = "Home",
                                onClick = { 
                                    viewModel.onIntent(QuizResultIntent.ResetQuiz)
                                    navController.popBackStack() 
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                item {
                    StatsSummary(
                        totalQuestions = state.quizResults.size,
                        correctAnswers = state.score,
                        incorrectAnswers = state.quizResults.size - state.score,
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.question_review),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(state.quizResults) { result ->
                    QuizResultCard(
                        question = result.question,
                        selectedAnswerIndex = result.selectedAnswerIndex,
                        correctAnswerIndex = result.correctAnswerIndex,
                        isCorrect = result.isCorrect,
                        explanationState = state.explanations[result.question.question],
                        isOnline = state.isOnline,
                        onExplainClick = {
                            viewModel.onIntent(
                                QuizResultIntent.GetExplanation(
                                    result.question.question,
                                    result.question.correctAnswer
                                )
                            )
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = if (quizResultId != null) "Loading result details..." else stringResource(R.string.no_quiz_results),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    if (quizResultId == null) {
                        PrimaryButton(
                            text = stringResource(R.string.take_a_quiz),
                            onClick = {
                                viewModel.onIntent(QuizResultIntent.ResetQuiz)
                                navController.popBackStack()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreCard(
    score: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier,
) {
    val percentage =
        if (totalQuestions > 0) {
            (score * 100) / totalQuestions
        } else {
            0
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.quiz_completed),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )

            Text(
                text = "$score / $totalQuestions",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.correct_percentage, percentage),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun StatsSummary(
    totalQuestions: Int,
    correctAnswers: Int,
    incorrectAnswers: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatBox(
            label = "Total",
            value = totalQuestions.toString(),
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        StatBox(
            label = "Correct",
            value = correctAnswers.toString(),
            containerColor = Color(0xFFE8F5E9),
            contentColor = Color(0xFF2E7D32),
            modifier = Modifier.weight(1f)
        )
        StatBox(
            label = "Wrong",
            value = incorrectAnswers.toString(),
            containerColor = Color(0xFFFFEBEE),
            contentColor = Color(0xFFC62828),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatBox(
    label: String,
    value: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = contentColor)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = contentColor.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun Surface(
    color: Color,
    shape: androidx.compose.ui.graphics.Shape,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.Surface(
        color = color,
        shape = shape,
        content = content
    )
}
