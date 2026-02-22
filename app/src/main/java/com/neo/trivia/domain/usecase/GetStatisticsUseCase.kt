package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetStatisticsUseCase @Inject constructor(
    private val repository: TriviaRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getAllQuestions()
            .map { questions ->
                questions.size
            }
    }
}