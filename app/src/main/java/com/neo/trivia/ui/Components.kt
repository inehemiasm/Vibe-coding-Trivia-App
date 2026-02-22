package com.neo.trivia.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import com.neo.trivia.domain.model.Category
// Design system imports
import com.neo.design.buttons.PrimaryButton
import com.neo.design.buttons.LoadingButton
import com.neo.design.buttons.OutlinedButton
import com.neo.design.buttons.SecondaryButton
import com.neo.design.buttons.TertiaryButton
import com.neo.design.cards.AppCard
import com.neo.design.cards.StatCard
import com.neo.design.icons.IconSize
import com.neo.design.icons.AppIcon
import com.neo.design.icons.TriviaIcons
import com.neo.design.typography.Typography

// Refactored components using design system
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    PrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    )
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
            PrimaryButton(
                text = if (currentIndex + 1 == totalQuestions) "Finish" else "Next",
                onClick = onNextClick,
                enabled = selectedAnswerIndex != null
            )
        },
        dismissButton = {
            if (currentIndex > 0) {
                OutlinedButton(
                    text = "Previous",
                    onClick = onPreviousClick
                )
            }
        }
    )
}