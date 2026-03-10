package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.repository.TriviaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncQuestionsUseCase
    @Inject
    constructor(
        private val repository: TriviaRepository,
        private val getCategoriesUseCase: GetCategoriesUseCase,
    ) {
        suspend operator fun invoke(targetAmountPerCategory: Int = 20) {
            val categoriesResult = getCategoriesUseCase()
            categoriesResult.onSuccess { categories ->
                repository.syncQuestions(categories, targetAmountPerCategory)
            }
        }
    }
