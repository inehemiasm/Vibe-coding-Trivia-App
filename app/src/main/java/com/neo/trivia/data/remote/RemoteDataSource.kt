package com.neo.trivia.data.remote

import com.neo.trivia.data.Result
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.domain.model.Question
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining remote data source operations.
 * Abstracts away the implementation details of API calls.
 * All operations use the Result wrapper for consistent error handling.
 *
 * @see RemoteDataSourceImpl for the implementation
 */
interface RemoteDataSource {
    /**
     * Fetches questions from the remote API.
     * Returns a Flow that emits loading state and results.
     * @param amount Number of questions to fetch (1-50)
     * @param category Optional category filter
     * @param difficulty Difficulty level (EASY, MEDIUM, HARD)
     * @return Flow containing Result<List<Question>> with loading state and data/error
     */
    fun getQuestions(
        amount: Int,
        category: Category?,
        difficulty: Difficulty
    ): Flow<Result<List<Question>>>

    /**
     * Fetches questions from the remote API synchronously.
     * @param amount Number of questions to fetch (1-50)
     * @param category Optional category filter
     * @param difficulty Difficulty level (EASY, MEDIUM, HARD)
     * @return Result containing list of Question objects or error
     */
    suspend fun getQuestionsSync(
        amount: Int,
        category: Category?,
        difficulty: Difficulty
    ): Result<List<Question>>

    /**
     * Fetches categories from the remote API.
     * @return Result containing list of Category objects or error
     */
    suspend fun getCategories(): Result<List<Category>>
}
