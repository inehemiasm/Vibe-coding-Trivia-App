package com.neo.trivia.ui.trivia

import androidx.lifecycle.viewModelScope
import com.neo.trivia.core.BaseViewModel
import com.neo.trivia.core.NetworkMonitor
import com.neo.trivia.core.UiEffect
import com.neo.trivia.core.UiIntent
import com.neo.trivia.core.UiState
import com.neo.trivia.data.api.ApiConstants
import com.neo.trivia.data.remote.TriviaAiManager
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.usecase.GetQuestionsUseCase
import com.neo.trivia.domain.usecase.SaveQuizResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel
    @Inject
    constructor(
        private val getQuestionsUseCase: GetQuestionsUseCase,
        private val saveQuizResultUseCase: SaveQuizResultUseCase,
        private val aiManager: TriviaAiManager,
        private val networkMonitor: NetworkMonitor,
    ) : BaseViewModel<QuestionUiState, QuestionIntent, QuestionUiEffect>(QuestionUiState()) {
        private var category: Category? = null

        init {
            viewModelScope.launch {
                networkMonitor.isOnline.collectLatest { isOnline ->
                    setState { copy(isOnline = isOnline) }
                }
            }

            viewModelScope.launch {
                aiManager.isQuotaExceeded.collectLatest { isQuotaExceeded ->
                    setState { copy(isAiQuotaExceeded = isQuotaExceeded) }
                }
            }
        }

        override suspend fun handleIntent(intent: QuestionIntent) {
            when (intent) {
                is QuestionIntent.LoadQuestions -> getQuestions(intent.amount, intent.category, intent.difficulty)
                is QuestionIntent.SelectAnswer -> onAnswerSelected(intent.answerIndex)
                is QuestionIntent.SubmitAnswer -> onSubmitAnswer()
                is QuestionIntent.ResetQuizState -> resetState()
                is QuestionIntent.GetAiHint -> getAiHint()
                is QuestionIntent.GetAnswerExplanation -> getAnswerExplanation(intent.question, intent.answer)
            }
        }

        private fun resetState() {
            setState { 
                QuestionUiState(
                    isOnline = currentState.isOnline,
                    isAiQuotaExceeded = currentState.isAiQuotaExceeded
                )
            }
        }

        private fun getQuestions(
            amount: Int,
            category: Category?,
            difficulty: Difficulty,
        ) {
            this.category = category
            viewModelScope.launch {
                setState { copy(isLoading = true, error = null, currentQuestionIndex = 0, score = 0, quizResults = emptyList(), isFinished = false) }
                try {
                    val questions = getQuestionsUseCase(amount, category, difficulty).getOrThrow()
                    setState { copy(isLoading = false, questions = questions) }
                    fetchImageForCurrentQuestion()
                } catch (e: Exception) {
                    setState { copy(isLoading = false, error = e.message ?: "An error occurred") }
                }
            }
        }

        private fun onAnswerSelected(answerIndex: Int) {
            setState { copy(selectedAnswerIndex = answerIndex) }
        }

        private fun onSubmitAnswer() {
            val state = currentState
            val selectedIndex = state.selectedAnswerIndex ?: return
            if (state.questions.isEmpty() || state.isFinished) return

            val currentQuestion = state.questions[state.currentQuestionIndex]
            val isCorrect = currentQuestion.correctAnswer == currentQuestion.answers[selectedIndex]

            val result =
                QuizResult(
                    question = currentQuestion,
                    selectedAnswerIndex = selectedIndex,
                    correctAnswerIndex = currentQuestion.answers.indexOf(currentQuestion.correctAnswer),
                    isCorrect = isCorrect,
                )

            val updatedQuizResults = state.quizResults + result
            val updatedScore = if (isCorrect) state.score + 1 else state.score

            setState { copy(hint = null, answerExplanation = null, selectedAnswerIndex = null, currentImageUrl = null) }

            if (state.currentQuestionIndex < state.questions.size - 1) {
                setState {
                    copy(
                        quizResults = updatedQuizResults,
                        score = updatedScore,
                        currentQuestionIndex = currentQuestionIndex + 1,
                    )
                }
                fetchImageForCurrentQuestion()
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

        private fun fetchImageForCurrentQuestion() {
            if (!currentState.isOnline || currentState.isAiQuotaExceeded) {
                Timber.d("fetchImageForCurrentQuestion: Skipping fetch (online: ${currentState.isOnline}, quotaExceeded: ${currentState.isAiQuotaExceeded})")
                return
            }
            
            val state = currentState
            val question = state.questions.getOrNull(state.currentQuestionIndex) ?: return
            
            viewModelScope.launch {
                Timber.d("fetchImageForCurrentQuestion: Requesting keyword for question: ${question.question}")
                val keyword = aiManager.generateImageKeyword(question.question)
                Timber.d("fetchImageForCurrentQuestion: Received keyword: $keyword")
                
                if (keyword != null) {
                    val imageUrl = "${ApiConstants.LOREM_FLICKR_BASE_URL}${keyword.replace(" ", ",")}"
                    Timber.d("fetchImageForCurrentQuestion: Generated image URL: $imageUrl")
                    setState { copy(currentImageUrl = imageUrl) }
                } else {
                    Timber.w("fetchImageForCurrentQuestion: Keyword generation failed")
                }
            }
        }

        private fun getAiHint() {
            if (!currentState.isOnline || currentState.isAiQuotaExceeded) return

            val state = currentState
            val currentQuestion = state.questions.getOrNull(state.currentQuestionIndex) ?: return
            
            viewModelScope.launch {
                setState { copy(isAiLoading = true) }
                val hint = aiManager.generateHint(currentQuestion.question, currentQuestion.answers)
                setState { copy(isAiLoading = false, hint = hint) }
            }
        }

        private fun getAnswerExplanation(question: String, answer: String) {
            if (!currentState.isOnline || currentState.isAiQuotaExceeded) return

            viewModelScope.launch {
                setState { copy(isAiLoading = true) }
                val explanation = aiManager.generateExplanation(question, answer)
                setState { copy(isAiLoading = false, answerExplanation = explanation) }
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
    val hint: String? = null,
    val answerExplanation: String? = null,
    val isAiLoading: Boolean = false,
    val isAiQuotaExceeded: Boolean = false,
    val isOnline: Boolean = true,
    val selectedAnswerIndex: Int? = null,
    val currentImageUrl: String? = null,
) : UiState

sealed class QuestionIntent : UiIntent {
    data class LoadQuestions(val amount: Int, val category: Category?, val difficulty: Difficulty) : QuestionIntent()

    data class SelectAnswer(val answerIndex: Int) : QuestionIntent()
    
    object SubmitAnswer : QuestionIntent()

    object ResetQuizState : QuestionIntent()

    object GetAiHint : QuestionIntent()

    data class GetAnswerExplanation(val question: String, val answer: String) : QuestionIntent()
}

sealed class QuestionUiEffect : UiEffect {
    data class NavigateToResults(val questions: List<Question>, val score: Int) : QuestionUiEffect()
}
