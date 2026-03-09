package com.neo.trivia.ui.trivia

import androidx.lifecycle.viewModelScope
import com.neo.trivia.core.BaseViewModel
import com.neo.trivia.core.UiEffect
import com.neo.trivia.core.UiIntent
import com.neo.trivia.core.UiState
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel
    @Inject
    constructor(
        private val getCategoriesUseCase: GetCategoriesUseCase,
    ) : BaseViewModel<CategoryUiState, CategoryIntent, CategoryUiEffect>(CategoryUiState()) {
        override suspend fun handleIntent(intent: CategoryIntent) {
            when (intent) {
                is CategoryIntent.LoadCategories -> loadCategories()
            }
        }

        private fun loadCategories() {
            viewModelScope.launch {
                setState { copy(isLoading = true, error = null) }
                try {
                    val categories = getCategoriesUseCase().getOrThrow()
                    setState { copy(isLoading = false, categories = categories) }
                } catch (e: Exception) {
                    setState { copy(isLoading = false, error = e.message ?: "Failed to load categories") }
                }
            }
        }
    }

data class CategoryUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String? = null,
) : UiState

sealed class CategoryIntent : UiIntent {
    object LoadCategories : CategoryIntent()
}

sealed class CategoryUiEffect : UiEffect
