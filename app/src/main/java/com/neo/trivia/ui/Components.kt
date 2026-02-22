package com.neo.trivia.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neo.trivia.domain.model.Category

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(text)
    }
}

@Composable
fun CategoryChip(
    category: Category,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterOptionChip(
        text = category.displayName,
        selected = selected,
        onClick = onClick
    )
}

@Composable
fun FilterOptionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        modifier = modifier
    )
}

@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterOptionChip(
        text = text,
        selected = selected,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun AnswerSheetDialog(
    question: com.neo.trivia.domain.model.Question,
    selectedAnswerIndex: Int?,
    currentIndex: Int,
    totalQuestions: Int,
    onAnswerSelected: (Int) -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onBackToTrivia: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onBackToTrivia,
        title = {
            Text(
                text = if (currentIndex + 1 == totalQuestions) "Quiz Complete!" else "Question ${currentIndex + 1} of $totalQuestions"
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                val allAnswers = question.incorrectAnswers + question.correctAnswer

                allAnswers.forEachIndexed { index, answer ->
                    FilterOptionChip(
                        text = answer,
                        selected = selectedAnswerIndex == index,
                        onClick = {
                            onAnswerSelected(index)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onNextClick,
                enabled = selectedAnswerIndex != null
            ) {
                Text(
                    text = if (currentIndex + 1 == totalQuestions) "Finish" else "Next"
                )
            }
        },
        dismissButton = {
            if (currentIndex > 0) {
                Button(onClick = onPreviousClick) {
                    Text("Previous")
                }
            }
        }
    )
}