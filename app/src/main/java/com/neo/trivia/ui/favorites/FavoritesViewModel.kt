package com.neo.trivia.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.usecase.GetFavoriteQuestionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteQuestionsUseCase: GetFavoriteQuestionsUseCase
) : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            getFavoriteQuestionsUseCase()
                .collect { questions ->
                    _questions.value = questions
                    _isLoading.value = false
                }
        }
    }

    fun onFavoriteToggled(question: Question) {
        viewModelScope.launch {
            _questions.value = _questions.value.filter { it.question != question.question }
        }
    }
}
