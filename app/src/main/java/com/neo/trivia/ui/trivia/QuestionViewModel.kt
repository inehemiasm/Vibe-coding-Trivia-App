package com.neo.trivia.ui.trivia

import androidx.lifecycle.viewModelScope
import com.neo.trivia.core.BaseViewModel
import com.neo.trivia.core.UiEffect
import com.neo.trivia.core.UiIntent
import com.neo.trivia.core.UiState
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.usecase.GetQuestionsUseCase
import com.neo.trivia.domain.usecase.SaveQuizResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel
    @Inject
    constructor(
        private val getQuestionsUseCase: GetQuestionsUseCase,
        private val saveQuizResultUseCase: SaveQuizResultUseCase,
    ) : BaseViewModel<QuestionUiState, QuestionIntent, QuestionUiEffect>(QuestionUiState()) {
        private var category: Category? = null

        override suspend fun handleIntent(intent: QuestionIntent) {
            when (intent) {
                is QuestionIntent.LoadQuestions -> getQuestions(intent.amount, intent.category, intent.difficulty)
                is QuestionIntent.SelectAnswer -> onAnswerSelected(intent.answerIndex)
            }
        }

        private fun getQuestions(
            amount: Int,
            category: Category?,
            difficulty: Difficulty,
        ) {
            this.category = category
            viewModelScope.launch {
                setState { copy(isLoading = true, error = null) }
                try {
                    val questions = getQuestionsUseCase(amount, category, difficulty).getOrThrow()
                    setState { copy(isLoading = false, questions = questions) }
                } catch (e: Exception) {
                    setState { copy(isLoading = false, error = e.message ?: "An error occurred") }
                }
            }
        }

        private fun onAnswerSelected(answerIndex: Int) {
            val state = currentState
            if (state.questions.isEmpty()) return

            val currentQuestion = state.questions[state.currentQuestionIndex]
            val isCorrect = currentQuestion.correctAnswer == currentQuestion.answers[answerIndex]

            val result =
                QuizResult(
                    question = currentQuestion,
                    selectedAnswerIndex = answerIndex,
                    correctAnswerIndex = currentQuestion.answers.indexOf(currentQuestion.correctAnswer),
                    isCorrect = isCorrect,
                )

            val updatedQuizResults = state.quizResults + result
            val updatedScore = if (isCorrect) state.score + 1 else state.score

            if (state.currentQuestionIndex < state.questions.size - 1) {
                setState {
                    copy(
                        quizResults = updatedQuizResults,
                        score = updatedScore,
                        currentQuestionIndex = currentQuestionIndex + 1,
                    )
                }
            } else {
                setState {
                    copy(
                        quizResults = updatedQuizResults,
                        score = updatedScore,
                        isFinished = true,
                    )
                }
                saveResultToDatabase(updatedScore, updatedQuizResults)
                sendEffect { QuestionUiEffect.NavigateToResults(state.questions, updatedScore) }
            }
        }

        private fun saveResultToDatabase(
            finalScore: Int,
            finalResults: List<QuizResult>,
        ) {
            val state = currentState
            category?.let { currentCategory ->
                viewModelScope.launch {
                    try {
                        saveQuizResultUseCase.save(
                            category = currentCategory,
                            score = finalScore,
                            totalQuestions = state.questions.size,
                            questions = state.questions,
                            quizResults = finalResults,
                        )
                        Timber.d("Quiz result saved to database")
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to save quiz result to database")
                    }
                }
            }
        }
    }

data class QuestionUiState(
    val isLoading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val quizResults: List<QuizResult> = emptyList(),
    val error: String? = null,
    val isFinished: Boolean = false,
) : UiState

sealed class QuestionIntent : UiIntent {
    data class LoadQuestions(val amount: Int, val category: Category?, val difficulty: Difficulty) : QuestionIntent()

    data class SelectAnswer(val answerIndex: Int) : QuestionIntent()
}

sealed class QuestionUiEffect : UiEffect {
    data class NavigateToResults(val questions: List<Question>, val score: Int) : QuestionUiEffect()
}
