package com.neo.trivia.ui.trivia

import com.neo.trivia.domain.model.Question

sealed class TriviaUiState {
    object Initial : TriviaUiState()
    object Loading : TriviaUiState()
    data class Success(val questions: List<Question>) : TriviaUiState()
    data class Error(val message: String) : TriviaUiState()
    object Finished : TriviaUiState()
}