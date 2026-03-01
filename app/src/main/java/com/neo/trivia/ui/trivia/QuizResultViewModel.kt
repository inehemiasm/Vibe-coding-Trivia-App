package com.neo.trivia.ui.trivia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.trivia.data.database.dao.QuizResultDao
import com.neo.trivia.data.local.LocalDataSource
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
class QuizResultViewModel @Inject constructor(
    private val saveQuizResultUseCase: SaveQuizResultUseCase,
    private val localDataSource: LocalDataSource
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

    fun setQuizData(questions: List<Question>, category: Category) {
        this.category = category
        _questions.value = questions
        _currentQuestionIndex.value = 0
        _score.value = 0
        _quizResults.value = emptyList()
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

    fun saveResultToDatabase() {
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

    fun resetQuiz() {
        _uiState.value = TriviaUiState.Initial
        _questions.value = emptyList()
        _currentQuestionIndex.value = 0
        _score.value = 0
        _quizResults.value = emptyList()
    }

    fun loadSavedResults() {
        viewModelScope.launch {
            localDataSource.getQuizResults().collect { results ->
                _quizResults.value = results
            }
        }
    }
}