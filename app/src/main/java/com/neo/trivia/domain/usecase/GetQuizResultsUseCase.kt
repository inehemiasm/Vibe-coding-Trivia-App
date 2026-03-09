package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuizResultsUseCase
    @Inject
    constructor(
        private val triviaRepository: TriviaRepository,
    ) {
        operator fun invoke(): Flow<List<QuizResult>> = triviaRepository.getQuizResults()

        suspend fun getById(id: String): Pair<List<Question>, List<QuizResult>>? {
            return triviaRepository.getQuizResultById(id)
        }
    }
