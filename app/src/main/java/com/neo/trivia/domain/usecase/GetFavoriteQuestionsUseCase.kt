package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavoriteQuestionsUseCase @Inject constructor(
    private val repository: TriviaRepository
) {
    operator fun invoke(): Flow<List<Question>> {
        return repository.getFavoriteQuestions()
    }
}