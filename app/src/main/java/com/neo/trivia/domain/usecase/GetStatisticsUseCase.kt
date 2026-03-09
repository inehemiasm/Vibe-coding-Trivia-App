package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.model.QuizHistory
import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetStatisticsUseCase
    @Inject
    constructor(
        private val repository: TriviaRepository,
    ) {
        fun getResultsHistory(): Flow<List<QuizHistory>> = repository.getQuizHistory()

        fun getQuizResults(): Flow<List<QuizResult>> = repository.getQuizResults()
    }
