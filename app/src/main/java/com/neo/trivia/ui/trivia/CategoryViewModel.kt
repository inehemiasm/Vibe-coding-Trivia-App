package com.neo.trivia.ui.trivia

import androidx.lifecycle.viewModelScope
import com.neo.trivia.core.BaseViewModel
import com.neo.trivia.core.UiEffect
import com.neo.trivia.core.UiIntent
import com.neo.trivia.core.UiState
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.usecase.GetCategoriesUseCase
import com.neo.trivia.domain.usecase.SyncQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel
    @Inject
    constructor(
        private val getCategoriesUseCase: GetCategoriesUseCase,
        private val syncQuestionsUseCase: SyncQuestionsUseCase,
    ) : BaseViewModel<CategoryUiState, CategoryIntent, CategoryUiEffect>(CategoryUiState()) {
        
        init {
            // Observe categories with questions to update UI reactively when offline data changes
            viewModelScope.launch {
                getCategoriesUseCase.getCategoriesWithQuestions().collectLatest { localCategories ->
                    if (currentState.error != null || currentState.categories.isEmpty()) {
                        // If we had an error (likely offline) or no categories yet, 
                        // update with what we have locally
                        if (localCategories.isNotEmpty()) {
                            setState { copy(categories = localCategories, isLoading = false, error = null) }
                        }
                    }
                }
            }
        }

        override suspend fun handleIntent(intent: CategoryIntent) {
            when (intent) {
                is CategoryIntent.LoadCategories -> loadCategories()
                is CategoryIntent.SyncOfflineData -> syncOfflineData()
            }
        }

        private fun loadCategories() {
            viewModelScope.launch {
                setState { copy(isLoading = true, error = null) }
                try {
                    val result = getCategoriesUseCase()
                    if (result.isSuccess) {
                        val categories = result.getOrThrow()
                        setState { copy(isLoading = false, categories = categories) }
                        // Automatically trigger sync after categories are loaded
                        onIntent(CategoryIntent.SyncOfflineData)
                    } else {
                        // If remote fails, the init block observer will pick up local categories 
                        // that have questions if they exist.
                        val error = result.exceptionOrNull()?.message ?: "Failed to load categories"
                        
                        // Check if we already have local data from the flow
                        if (currentState.categories.isEmpty()) {
                            setState { copy(isLoading = false, error = error) }
                        } else {
                            setState { copy(isLoading = false) }
                        }
                    }
                } catch (e: Exception) {
                    if (currentState.categories.isEmpty()) {
                        setState { copy(isLoading = false, error = e.message ?: "Failed to load categories") }
                    } else {
                        setState { copy(isLoading = false) }
                    }
                }
            }
        }

        private fun syncOfflineData() {
            viewModelScope.launch {
                setState { copy(isSyncing = true) }
                try {
                    syncQuestionsUseCase(targetAmountPerCategory = 20)
                    setState { copy(isSyncing = false) }
                } catch (e: Exception) {
                    setState { copy(isSyncing = false) }
                }
            }
        }
    }

data class CategoryUiState(
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String? = null,
) : UiState

sealed class CategoryIntent : UiIntent {
    object LoadCategories : CategoryIntent()
    object SyncOfflineData : CategoryIntent()
}

sealed class CategoryUiEffect : UiEffect
