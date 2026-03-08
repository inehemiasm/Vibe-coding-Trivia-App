package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuizResultByIdUseCase @Inject constructor(
    private val triviaRepository: TriviaRepository
) {
    suspend fun get(id: String): Flow<QuizResult?> = triviaRepository.getLatestQuizResult()
}