package com.neo.trivia.ui.trivia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.usecase.GetQuestionsUseCase
import com.neo.trivia.domain.usecase.SaveQuizResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val saveQuizResultUseCase: SaveQuizResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TriviaUiState>(TriviaUiState.Initial)
    val uiState: StateFlow<TriviaUiState> = _uiState.asStateFlow()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val currentQuestions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _quizResults = MutableStateFlow<List<QuizResult>>(emptyList())
    val quizResults: StateFlow<List<QuizResult>> = _quizResults.asStateFlow()

    private var category: Category? = null

    fun getQuestions(amount: Int, category: Category?, difficulty: Difficulty) {
        this.category = category
        viewModelScope.launch {
            _uiState.value = TriviaUiState.Loading
            try {
                val questions = getQuestionsUseCase(amount, category, difficulty).getOrThrow()
                _questions.value = questions
                _uiState.value = TriviaUiState.Success(questions)
            } catch (e: Exception) {
                _uiState.value = TriviaUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun onAnswerSelected(answerIndex: Int) {
        val currentQuestion = _questions.value[_currentQuestionIndex.value]
        val isCorrect = currentQuestion.correctAnswer == currentQuestion.answers[answerIndex]

        val result = QuizResult(
            question = currentQuestion,
            selectedAnswerIndex = answerIndex,
            correctAnswerIndex = currentQuestion.answers.indexOf(currentQuestion.correctAnswer),
            isCorrect = isCorrect
        )

        _quizResults.value = _quizResults.value + result

        if (isCorrect) {
            _score.value++
        }

        if (_currentQuestionIndex.value < _questions.value.size - 1) {
            _currentQuestionIndex.value++
        } else {
            _uiState.value = TriviaUiState.Finished
            saveResultToDatabase()
        }
    }

    private fun saveResultToDatabase() {
        category?.let { currentCategory ->
            viewModelScope.launch {
                try {
                    saveQuizResultUseCase.save(
                        category = currentCategory,
                        score = _score.value,
                        totalQuestions = _questions.value.size,
                        questions = _questions.value,
                        quizResults = _quizResults.value
                    )
                    Timber.d("Quiz result saved to database")
                } catch (e: Exception) {
                    Timber.e(e, "Failed to save quiz result to database")
                }
            }
        }
    }
}
