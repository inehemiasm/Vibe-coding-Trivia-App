package com.neo.trivia.ui.trivia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.usecase.GetQuizResultsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val getQuizResultsUseCase: GetQuizResultsUseCase
) : ViewModel() {
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _quizResults = MutableStateFlow<List<QuizResult>>(emptyList())
    val quizResults: StateFlow<List<QuizResult>> = _quizResults.asStateFlow()

    fun resetQuiz() {
        _score.value = 0
        _quizResults.value = emptyList()
    }

    fun loadSavedResults() {
        viewModelScope.launch {
            getQuizResultsUseCase().collect { results ->
                Timber.d("Loaded quiz results: $results")
                _quizResults.value = results
                val score = results.count { it.isCorrect }
                _score.value = score
            }
        }
    }

    fun loadResultById(id: String) {
        viewModelScope.launch {
            val result = getQuizResultsUseCase.getById(id)
            if (result != null) {
                Timber.d("Loaded quiz result by ID: $id, result: $result")
                _quizResults.value = result.second
                _score.value = result.second.count { it.isCorrect }
            }
        }
    }
}
