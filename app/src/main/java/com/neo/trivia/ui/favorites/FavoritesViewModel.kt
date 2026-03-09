package com.neo.trivia.ui.favorites

import androidx.lifecycle.viewModelScope
import com.neo.trivia.core.BaseViewModel
import com.neo.trivia.core.UiEffect
import com.neo.trivia.core.UiIntent
import com.neo.trivia.core.UiState
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.usecase.GetFavoriteQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
    @Inject
    constructor(
        private val getFavoriteQuestionsUseCase: GetFavoriteQuestionsUseCase,
    ) : BaseViewModel<FavoritesUiState, FavoritesIntent, FavoritesUiEffect>(FavoritesUiState()) {
        init {
            onIntent(FavoritesIntent.LoadFavorites)
        }

        override suspend fun handleIntent(intent: FavoritesIntent) {
            when (intent) {
                is FavoritesIntent.LoadFavorites -> loadFavorites()
                is FavoritesIntent.ToggleFavorite -> onFavoriteToggled(intent.question)
            }
        }

        private fun loadFavorites() {
            viewModelScope.launch {
                setState { copy(isLoading = true) }
                getFavoriteQuestionsUseCase()
                    .collect { questions ->
                        setState { copy(questions = questions, isLoading = false) }
                    }
            }
        }

        private fun onFavoriteToggled(question: Question) {
            // Optimistic update or just wait for the flow from use case if it's connected to DB
            // The previous implementation just filtered the local list.
            setState {
                copy(questions = questions.filter { it.question != question.question })
            }
        }
    }

data class FavoritesUiState(
    val questions: List<Question> = emptyList(),
    val isLoading: Boolean = false,
) : UiState

sealed class FavoritesIntent : UiIntent {
    object LoadFavorites : FavoritesIntent()

    data class ToggleFavorite(val question: Question) : FavoritesIntent()
}

sealed class FavoritesUiEffect : UiEffect
