package com.neo.trivia.ui.trivia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _categoriesState = MutableStateFlow<CategoriesScreenState>(CategoriesScreenState.Loading)
    val categoriesState: StateFlow<CategoriesScreenState> = _categoriesState.asStateFlow()

   fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = CategoriesScreenState.Loading
            try {
                val categories = getCategoriesUseCase().getOrThrow()
                _categoriesState.value = CategoriesScreenState.Success(categories)
            } catch (e: Exception) {
                _categoriesState.value = CategoriesScreenState.Error(e.message ?: "Failed to load categories")
            }
        }
    }
}

sealed class CategoriesScreenState {
    object Loading : CategoriesScreenState()
    data class Success(val categories: List<Category>) : CategoriesScreenState()
    data class Error(val message: String) : CategoriesScreenState()
}