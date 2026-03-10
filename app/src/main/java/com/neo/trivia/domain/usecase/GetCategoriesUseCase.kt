package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase
    @Inject
    constructor(
        private val triviaRepository: TriviaRepository,
    ) {
        suspend operator fun invoke() = triviaRepository.getCategories()

        /**
         * Returns a flow of categories that have questions saved locally.
         */
        fun getCategoriesWithQuestions(): Flow<List<Category>> {
            return triviaRepository.getCategoriesWithQuestions()
        }
    }
