package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.repository.TriviaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetQuestionsUseCase @Inject constructor(
    private val repository: TriviaRepository
) {
    suspend operator fun invoke(
        amount: Int, 
        category: Category? = null, 
        difficulty: Difficulty
    ): Result<List<Question>> {
        return repository.getQuestions(amount, category, difficulty)
    }
}