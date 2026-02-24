package com.neo.trivia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.neo.design.buttons.PrimaryButton
import com.neo.trivia.R
import com.neo.trivia.ui.Components.CollapsibleSection
import com.neo.trivia.ui.Components.QuizResultCard
import com.neo.trivia.ui.trivia.TriviaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultScreen(
    viewModel: TriviaViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    val score = viewModel.score.collectAsStateWithLifecycle().value
    val quizResults = viewModel.quizResults.collectAsStateWithLifecycle().value


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.quiz_results_top_bar_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (true && quizResults.isNotEmpty()) {
                // Score display
                ScoreCard(score = score, totalQuestions = quizResults.size)

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.resetQuiz()
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.quiz_retake))
                    }

                    PrimaryButton(
                        text = stringResource(R.string.share_results),
                        onClick = { /* TODO: Implement share functionality */ },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Results summary
                CollapsibleSection(
                    title = stringResource(R.string.results_summary),
                    titleColor = MaterialTheme.colorScheme.onBackground
                ) {
                    StatsSummary(
                        totalQuestions = quizResults.size,
                        correctAnswers = score,
                        incorrectAnswers = quizResults.size - score
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Questions list
                Text(
                    text = stringResource(R.string.question_review),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(quizResults) { result ->
                        QuizResultCard(
                            question = result.question,
                            selectedAnswerIndex = result.selectedAnswerIndex,
                            correctAnswerIndex = result.correctAnswerIndex,
                            isCorrect = result.isCorrect
                        )
                    }
                }

            } else {
                // No results available
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.no_quiz_results),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        PrimaryButton(
                            text = stringResource(R.string.take_a_quiz),
                            onClick = {
                                viewModel.resetQuiz()
                                navController.popBackStack()
                            }
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
    modifier: Modifier = Modifier
) {
    val percentage = if (totalQuestions > 0) {
        (score * 100) / totalQuestions
    } else 0

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.quiz_completed),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$score / $totalQuestions",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.correct_percentage, percentage),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun StatsSummary(
    totalQuestions: Int,
    correctAnswers: Int,
    incorrectAnswers: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatItem(
                label = R.string.stat_total_questions_label,
                value = totalQuestions.toString(),
                modifier = Modifier.weight(1f)
            )

            StatItem(
                label = R.string.stat_correct_label,
                value = correctAnswers.toString(),
                valueColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            StatItem(
                label = R.string.stat_incorrect_label,
                value = incorrectAnswers.toString(),
                valueColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatItem(
    label: Int,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}