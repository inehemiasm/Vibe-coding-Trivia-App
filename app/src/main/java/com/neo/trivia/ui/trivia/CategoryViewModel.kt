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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel
    @Inject
    constructor(
        private val getCategoriesUseCase: GetCategoriesUseCase,
        private val syncQuestionsUseCase: SyncQuestionsUseCase,
    ) : BaseViewModel<CategoryUiState, CategoryIntent, CategoryUiEffect>(CategoryUiState()) {
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
                    val categories = getCategoriesUseCase().getOrThrow()
                    setState { copy(isLoading = false, categories = categories) }
                    // Automatically trigger sync after categories are loaded
                    onIntent(CategoryIntent.SyncOfflineData)
                } catch (e: Exception) {
                    setState { copy(isLoading = false, error = e.message ?: "Failed to load categories") }
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
                    // We don't necessarily want to block the UI if sync fails
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
