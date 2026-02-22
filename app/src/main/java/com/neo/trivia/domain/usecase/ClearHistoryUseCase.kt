package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.repository.TriviaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClearHistoryUseCase @Inject constructor(
    private val repository: TriviaRepository
) {
    suspend operator fun invoke() {
        repository.clearCache()
    }
}