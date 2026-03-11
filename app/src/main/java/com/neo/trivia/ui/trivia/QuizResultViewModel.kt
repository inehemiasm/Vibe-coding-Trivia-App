package com.neo.trivia.ui.trivia

import androidx.lifecycle.viewModelScope
import com.neo.trivia.core.BaseViewModel
import com.neo.trivia.core.UiEffect
import com.neo.trivia.core.UiIntent
import com.neo.trivia.core.UiState
import com.neo.trivia.data.remote.TriviaAiManager
import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.usecase.GetQuizResultsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel
    @Inject
    constructor(
        private val getQuizResultsUseCase: GetQuizResultsUseCase,
        private val aiManager: TriviaAiManager,
    ) : BaseViewModel<QuizResultUiState, QuizResultIntent, QuizResultUiEffect>(QuizResultUiState()) {
        override suspend fun handleIntent(intent: QuizResultIntent) {
            when (intent) {
                is QuizResultIntent.LoadSavedResults -> loadSavedResults()
                is QuizResultIntent.LoadResultById -> loadResultById(intent.id)
                is QuizResultIntent.ResetQuiz -> resetQuiz()
                is QuizResultIntent.GetExplanation -> getExplanation(intent.question, intent.answer)
            }
        }

        private fun resetQuiz() {
            setState { copy(score = 0, quizResults = emptyList(), explanations = emptyMap()) }
        }

        private fun loadSavedResults() {
            viewModelScope.launch {
                setState { copy(isLoading = true) }
                getQuizResultsUseCase().collect { results ->
                    Timber.d("Loaded quiz results: $results")
                    val score = results.count { it.isCorrect }
                    setState { copy(isLoading = false, quizResults = results, score = score) }
                }
            }
        }

        private fun loadResultById(id: String) {
            viewModelScope.launch {
                setState { copy(isLoading = true) }
                val result = getQuizResultsUseCase.getById(id)
                if (result != null) {
                    Timber.d("Loaded quiz result by ID: $id, result: $result")
                    val score = result.second.count { it.isCorrect }
                    setState { copy(isLoading = false, quizResults = result.second, score = score) }
                } else {
                    setState { copy(isLoading = false, error = "Result not found") }
                }
            }
        }

        private fun getExplanation(question: String, answer: String) {
            viewModelScope.launch {
                setState { copy(explanations = explanations + (question to ExplanationState.Loading)) }
                val explanation = aiManager.generateExplanation(question, answer)
                if (explanation != null) {
                    setState { copy(explanations = explanations + (question to ExplanationState.Success(explanation))) }
                } else {
                    setState { copy(explanations = explanations + (question to ExplanationState.Error)) }
                }
            }
        }
    }

data class QuizResultUiState(
    val score: Int = 0,
    val quizResults: List<QuizResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val explanations: Map<String, ExplanationState> = emptyMap()
) : UiState

sealed class ExplanationState {
    object Loading : ExplanationState()
    data class Success(val text: String) : ExplanationState()
    object Error : ExplanationState()
}

sealed class QuizResultIntent : UiIntent {
    object LoadSavedResults : QuizResultIntent()

    data class LoadResultById(val id: String) : QuizResultIntent()

    object ResetQuiz : QuizResultIntent()

    data class GetExplanation(val question: String, val answer: String) : QuizResultIntent()
}

sealed class QuizResultUiEffect : UiEffect
