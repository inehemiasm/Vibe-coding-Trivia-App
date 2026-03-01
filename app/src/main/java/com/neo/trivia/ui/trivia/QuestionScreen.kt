package com.neo.trivia.ui.trivia

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.neo.trivia.R
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.domain.model.Question
import com.neo.trivia.ui.Components
import com.neo.trivia.ui.QuizResultScreen
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    viewModel: QuestionViewModel = hiltViewModel(),
    navController: NavController,
    category: Category,
    difficulty: Difficulty,
    onQuizFinished: (List<Question>, Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Load questions when entering the screen
    LaunchedEffect(category, difficulty) {
        viewModel.getQuestions(10, category, difficulty)
    }

    // Navigate to results when quiz is finished
    LaunchedEffect(uiState) {
        if (uiState is TriviaUiState.Finished) {
            val questions = viewModel.currentQuestions.value
            val score = viewModel.score.value
            onQuizFinished(questions, score)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Question") },
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
        Timber.d("Testing1: State: $uiState")

        when (val state = uiState) {
            is TriviaUiState.Loading, TriviaUiState.Initial -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is TriviaUiState.Success -> {
                val questions = state.questions

                val selectedQuestionIndex by viewModel.currentQuestionIndex.collectAsStateWithLifecycle()
                val currentQuestion = questions.getOrNull(selectedQuestionIndex)
                Timber.d("Testing1: Questions: $questions")
                Timber.d("Testing1: selectedQuestionIndex: $selectedQuestionIndex")
                Timber.d("Testing1: currentQuestion: $currentQuestion")


                if (currentQuestion != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.quiz_question_count, selectedQuestionIndex + 1, questions.size),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = currentQuestion.question,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        val allAnswers = currentQuestion.answers

                        allAnswers.forEachIndexed { index, answer ->
                            Components.FilterOptionChip(
                                text = answer,
                                selected = false,
                                onClick = {
                                    viewModel.onAnswerSelected(index)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            is TriviaUiState.Error -> {
                // Handle error state
            }
            else -> {}
        }
    }
}
