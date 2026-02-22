package com.neo.trivia.ui.trivia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.usecase.GetQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TriviaViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    private val _questionCount = MutableStateFlow(10)
    val questionCount: StateFlow<Int> = _questionCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _currentQuestions = MutableStateFlow<List<Question>?>(null)
    val currentQuestions: StateFlow<List<Question>?> = _currentQuestions.asStateFlow()

    private val _selectedQuestionIndex = MutableStateFlow<Int?>(null)
    val selectedQuestionIndex: StateFlow<Int?> = _selectedQuestionIndex.asStateFlow()

    private val _selectedAnswers = MutableStateFlow<Map<Int, Int>?>(null)
    val selectedAnswers: StateFlow<Map<Int, Int>?> = _selectedAnswers.asStateFlow()

    val totalQuestions: Int
        get() = currentQuestions.value?.size ?: 0

    fun selectCategory(category: Category) {
        _selectedCategory.value = category
    }

    fun setQuestionCount(count: Int) {
        _questionCount.value = count
    }

    fun loadQuestions(questionCount: Int, category: Category?) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getQuestionsUseCase(questionCount, category)
            result.onSuccess {
                _currentQuestions.value = it
                _selectedQuestionIndex.value = 0
            }.onFailure {
                _errorMessage.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun selectAnswer(questionIndex: Int, answerIndex: Int) {
        val newAnswers = _selectedAnswers.value?.toMutableMap() ?: mutableMapOf()
        newAnswers[questionIndex] = answerIndex
        _selectedAnswers.value = newAnswers
    }

    fun nextQuestion() {
        _selectedQuestionIndex.value = (_selectedQuestionIndex.value ?: 0) + 1
    }

    fun previousQuestion() {
        _selectedQuestionIndex.value = (_selectedQuestionIndex.value ?: 0) - 1
    }

    fun resetQuestionIndex() {
        _selectedQuestionIndex.value = null
    }
}