package com.neo.trivia.data.local

import com.neo.trivia.data.database.dao.FavoriteDao
import com.neo.trivia.data.database.dao.QuestionDao
import com.neo.trivia.data.database.dao.QuizResultDao
import com.neo.trivia.data.database.entity.FavoriteEntity
import com.neo.trivia.data.database.entity.QuestionEntity
import com.neo.trivia.data.database.entity.QuizResultEntity
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local data source implementation using Room database.
 * Handles all local database operations including:
 * - Question caching and retrieval
 * - Favorite question management
 * - Search functionality
 * - Quiz history tracking
 *
 * This implementation converts between Room entities and domain Question models.
 */
@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val favoriteDao: FavoriteDao,
    private val quizResultDao: QuizResultDao
) : LocalDataSource {

    /**
     * Converts domain Question objects to Room QuestionEntity objects.
     */
    private fun questionToEntity(question: Question): QuestionEntity {
        return QuestionEntity(
            id = question.id,
            question = question.question,
            correctAnswer = question.correctAnswer,
            incorrectAnswers = question.answers,
            category = question.category,
            type = question.type
        )
    }

    /**
     * Converts Room QuestionEntity objects to domain Question objects.
     */
    private fun entityToQuestion(entity: QuestionEntity): Question {
        return Question(
            id = entity.id,
            question = entity.question,
            correctAnswer = entity.correctAnswer,
            answers = entity.incorrectAnswers,
            category = entity.category,
            type = entity.type,
        )
    }

    override suspend fun insertQuestions(questions: List<Question>) {
        val questionEntities = questions.map { questionToEntity(it) }
        questionDao.insertAll(questionEntities)
    }

    override fun getAllQuestions(): Flow<List<Question>> {
        return questionDao.getAllQuestions().map { entities ->
            entities.map { entityToQuestion(it) }
        }
    }

    override fun searchQuestions(query: String): Flow<List<Question>> {
        return questionDao.searchQuestions(query).map { entities ->
            entities.map { entityToQuestion(it) }
        }
    }

    override suspend fun clearCache() {
        questionDao.clearAll()
    }

    override suspend fun isFavorite(questionId: String): Boolean {
        return favoriteDao.isFavorite(questionId)
    }

    override suspend fun toggleFavorite(questionId: String): Boolean {
        val isFavorite = favoriteDao.isFavorite(questionId)
        return if (isFavorite) {
            favoriteDao.removeFavorite(questionId)
            false
        } else {
            favoriteDao.insert(FavoriteEntity(questionId = questionId))
            true
        }
    }

    override fun getFavoriteQuestions(): Flow<List<Question>> {
        return favoriteDao.getFavoriteQuestions().map { entities ->
            entities.map { entityToQuestion(it) }
        }
    }

    override suspend fun removeFavorite(questionId: String) {
        favoriteDao.removeFavorite(questionId)
    }

    override suspend fun insertFavorite(questionId: String) {
        favoriteDao.insert(FavoriteEntity(questionId = questionId))
    }

    /**
     * Converts Room QuizResultEntity objects to domain QuizResult objects.
     * Note: Question and QuizResult objects are stored as JSON strings due to Room limitations.
     * For now, we'll just extract the basic question structure.
     */
    private fun entityToQuizResult(entity: QuizResultEntity): QuizResult {
        val questions = com.google.gson.Gson().fromJson(entity.questionsJson, Array<Question>::class.java)
        val defaultCategoryName = "General Knowledge"
        return QuizResult(
            question = questions.firstOrNull() ?: Question("", "", emptyList(), defaultCategoryName, defaultCategoryName, "multiple"),
            selectedAnswerIndex = -1, // Cannot retrieve from JSON
            correctAnswerIndex = -1,  // Cannot retrieve from JSON
            isCorrect = true           // Cannot retrieve from JSON
        )
    }

    override fun getQuizResults(): Flow<List<com.neo.trivia.domain.model.QuizResult>> {
        return quizResultDao.getRecentResults().map { entities ->
            entities.map { entityToQuizResult(it) }
        }
    }

    override fun getLatestQuizResult(): Flow<com.neo.trivia.domain.model.QuizResult?> {
        return quizResultDao.getLatestResult().map { entity ->
            entity?.let { entityToQuizResult(it) }
        }
    }
}