package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.repository.TriviaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToggleFavoriteUseCase @Inject constructor(
    private val repository: TriviaRepository
) {
    suspend operator fun invoke(question: Question): Boolean {
        return repository.toggleFavorite(question)
    }
}