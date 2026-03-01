package com.neo.trivia.data.local

import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining local data source operations.
 * Abstracts away the implementation details of database operations.
 * All operations return data in the domain model format (Question).
 */
interface LocalDataSource {
    /**
     * Inserts a list of questions into the local database.
     * @param questions List of domain questions to insert
     */
    suspend fun insertQuestions(questions: List<Question>)

    /**
     * Retrieves all cached questions from the local database.
     * @return Flow of domain Question objects with reactive updates
     */
    fun getAllQuestions(): Flow<List<Question>>

    /**
     * Searches for questions containing the specified query.
     * @param query Search query string for text matching
     * @return Flow of matching domain Question objects
     */
    fun searchQuestions(query: String): Flow<List<Question>>

    /**
     * Clears all cached questions from the local database.
     * Use with caution - this removes all cached questions.
     */
    suspend fun clearCache()

    /**
     * Checks if a question ID exists in favorites.
     * @param questionId Question ID to check
     * @return True if favorited, false otherwise
     */
    suspend fun isFavorite(questionId: String): Boolean

    /**
     * Toggles the favorite status of a question.
     * @param questionId Question ID to toggle
     * @return True if now favorited, false if now unfavorited
     */
    suspend fun toggleFavorite(questionId: String): Boolean

    /**
     * Retrieves all favorited questions.
     * @return Flow of favorited domain Question objects
     */
    fun getFavoriteQuestions(): Flow<List<Question>>

    /**
     * Removes a question from favorites.
     * @param questionId Question ID to remove
     */
    suspend fun removeFavorite(questionId: String)

    /**
     * Inserts a question into favorites.
     * @param questionId Question ID to add
     */
    suspend fun insertFavorite(questionId: String)

    /**
     * Retrieves all recent quiz results.
     * @return Flow of domain QuizResult objects
     */
    fun getQuizResults(): Flow<List<QuizResult>>

    /**
     * Retrieves the most recent quiz result.
     * @return Flow of the latest domain QuizResult object or null
     */
    fun getLatestQuizResult(): Flow<QuizResult?>
}