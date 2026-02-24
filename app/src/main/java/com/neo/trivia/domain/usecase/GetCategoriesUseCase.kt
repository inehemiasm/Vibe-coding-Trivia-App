package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.repository.TriviaRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val triviaRepository: TriviaRepository
) {
    suspend operator fun invoke() = triviaRepository.getCategories()
}