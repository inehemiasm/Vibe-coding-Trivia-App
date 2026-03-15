package com.neo.trivia.domain.repository

import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizHistory
import com.neo.trivia.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow

interface TriviaRepository {
    suspend fun getQuestions(
        amount: Int,
        category: Category? = null,
        difficulty: Difficulty,
    ): kotlin.Result<List<Question>>

    suspend fun getCategories(): kotlin.Result<List<Category>>

    suspend fun clearCache()

    suspend fun toggleFavorite(question: Question): Boolean

    fun getFavoriteQuestions(): Flow<List<Question>>

    suspend fun save(
        category: Category,
        score: Int,
        totalQuestions: Int,
        questions: List<Question>,
        quizResults: List<QuizResult>,
    )

    /**
     * Retrieves the results of the most recent quiz session.
     */
    fun getQuizResults(): Flow<List<QuizResult>>

    /**
     * Retrieves the history of all quiz sessions.
     */
    fun getQuizHistory(): Flow<List<QuizHistory>>

    fun getLatestQuizResult(): Flow<QuizResult?>

    suspend fun getQuizResultById(id: String): Pair<List<Question>, List<QuizResult>>?

    suspend fun getQuestionsOffline(
        category: Category,
        difficulty: Difficulty,
        amount: Int,
    ): List<Question>

    suspend fun syncQuestions(categories: List<Category>, targetAmountPerCategory: Int)

    fun getCategoriesWithQuestions(): Flow<List<Category>>
}
