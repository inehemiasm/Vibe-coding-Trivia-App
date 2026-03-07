package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetStatisticsUseCase @Inject constructor(
    private val repository: TriviaRepository
) {
    // For now, let's just return the raw quiz results for display
    fun getQuizResults(): Flow<List<QuizResult>> = repository.getQuizResults()
}