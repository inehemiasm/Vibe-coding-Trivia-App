package com.neo.trivia.ui.trivia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neo.design.buttons.LoadingButton
import com.neo.trivia.domain.model.Category
import com.neo.trivia.ui.AnswerSheetDialog
import com.neo.trivia.ui.CategoryChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaScreen(
    viewModel: TriviaViewModel = hiltViewModel(),
    onQuestionClick: (List<String>, Int) -> Unit = { _, _ -> }
) {
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val questionCount by viewModel.questionCount.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val currentQuestions by viewModel.currentQuestions.collectAsStateWithLifecycle()
    val selectedQuestionIndex by viewModel.selectedQuestionIndex.collectAsStateWithLifecycle()
    val selectedAnswers by viewModel.selectedAnswers.collectAsStateWithLifecycle()

    // Auto-load questions when category or question count changes
    LaunchedEffect(selectedCategory, questionCount) {
        selectedCategory?.let { category ->
            viewModel.loadQuestions(questionCount, category)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trivia App") }
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
            // Category Selection
            Text(
                text = "Select Category",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(Category.entries.toTypedArray()) { category ->
                    CategoryChip(
                        category = category,
                        selected = selectedCategory == category,
                        onClick = {
                            viewModel.selectCategory(category)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question Count Selection
            Text(
                text = "Number of Questions",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(5) { index ->
                    val count = (index + 1) * 10
                    com.neo.trivia.ui.FilterButton(
                        text = "$count",
                        selected = questionCount == count,
                        onClick = {
                            viewModel.setQuestionCount(count)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Start Quiz Button
            LoadingButton(
                text = "Start Quiz",
                onClick = {
                    viewModel.loadQuestions(questionCount, selectedCategory)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            // Status message
            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Current questions
            currentQuestions?.let { questions ->
                if (questions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your Questions (${questions.size})",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }

    selectedQuestionIndex?.let { index ->
        if (index >= 0 && index < (currentQuestions?.size ?: 0)) {
            val question = currentQuestions!![index]
            val selectedAnswerIndex = selectedAnswers?.get(index)

            AnswerSheetDialog(
                question = question,
                selectedAnswerIndex = selectedAnswerIndex,
                currentIndex = index,
                totalQuestions = viewModel.totalQuestions,
                onAnswerSelected = { answer ->
                    viewModel.selectAnswer(index, answer)
                },
                onNextClick = {
                    viewModel.nextQuestion()
                },
                onPreviousClick = {
                    viewModel.previousQuestion()
                },
                onBackToTrivia = {
                    viewModel.resetQuestionIndex()
                }
            )
        }
    }
}