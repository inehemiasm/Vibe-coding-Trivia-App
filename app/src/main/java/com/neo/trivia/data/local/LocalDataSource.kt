package com.neo.trivia.data.local

import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.model.QuizHistory
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining local data source operations.
 */
interface LocalDataSource {
    suspend fun insertQuestions(questions: List<Question>)
    fun getAllQuestions(): Flow<List<Question>>
    fun searchQuestions(query: String): Flow<List<Question>>
    suspend fun clearCache()
    suspend fun isFavorite(questionId: String): Boolean
    suspend fun toggleFavorite(questionId: String): Boolean
    fun getFavoriteQuestions(): Flow<List<Question>>
    suspend fun removeFavorite(questionId: String)
    suspend fun insertFavorite(questionId: String)

    /**
     * Retrieves the results of the most recent quiz session.
     */
    fun getQuizResults(): Flow<List<QuizResult>>

    /**
     * Retrieves the history of all quiz sessions.
     */
    fun getQuizHistory(): Flow<List<QuizHistory>>

    fun getLatestQuizResult(): Flow<QuizResult?>

    suspend fun saveQuizResult(
        category: Category,
        score: Int,
        totalQuestions: Int,
        questions: List<Question>,
        quizResults: List<QuizResult>
    )
}
