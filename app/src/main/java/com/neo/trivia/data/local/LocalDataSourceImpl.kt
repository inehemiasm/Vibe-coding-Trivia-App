package com.neo.trivia.data.local

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.neo.trivia.data.database.dao.FavoriteDao
import com.neo.trivia.data.database.dao.QuestionDao
import com.neo.trivia.data.database.dao.QuizResultDao
import com.neo.trivia.data.database.entity.FavoriteEntity
import com.neo.trivia.data.database.entity.QuestionEntity
import com.neo.trivia.data.database.entity.QuizResultEntity
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizHistory
import com.neo.trivia.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl
    @Inject
    constructor(
        private val questionDao: QuestionDao,
        private val favoriteDao: FavoriteDao,
        private val quizResultDao: QuizResultDao,
        private val gson: Gson,
    ) : LocalDataSource {
        private fun questionToEntity(question: Question): QuestionEntity {
            return QuestionEntity(
                id = question.id,
                question = question.question,
                correctAnswer = question.correctAnswer,
                incorrectAnswers = question.answers,
                category = question.category,
                type = question.type,
            )
        }

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

        private fun deserializeQuizResults(json: String): List<QuizResult> {
            return try {
                val type = object : TypeToken<List<QuizResult>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                emptyList()
            }
        }

        private fun deserializeQuestions(json: String): List<Question> {
            return try {
                val type = object : TypeToken<List<Question>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                emptyList()
            }
        }

        override fun getQuizResults(): Flow<List<QuizResult>> {
            return quizResultDao.getLatestResult().map { entity ->
                entity?.quizResultsJson?.let { deserializeQuizResults(it) } ?: emptyList()
            }
        }

        override fun getQuizHistory(): Flow<List<QuizHistory>> {
            return quizResultDao.getRecentResults().map { entities ->
                entities.map { QuizHistory.fromQuizResultEntity(it) }
            }
        }

        override fun getLatestQuizResult(): Flow<QuizResult?> {
            return quizResultDao.getLatestResult().map { entity ->
                entity?.quizResultsJson?.let { deserializeQuizResults(it).firstOrNull() }
            }
        }

        override suspend fun saveQuizResult(
            category: Category,
            score: Int,
            totalQuestions: Int,
            questions: List<Question>,
            quizResults: List<QuizResult>,
        ) {
            val entity =
                QuizResultEntity.from(
                    category = category,
                    score = score,
                    totalQuestions = totalQuestions,
                    questions = questions,
                    quizResults = quizResults,
                )
            quizResultDao.insert(entity)
        }

        override suspend fun getQuizResultById(id: String): Pair<List<Question>, List<QuizResult>>? {
            val entity = quizResultDao.getResultById(id) ?: return null
            val questions = deserializeQuestions(entity.questionsJson)
            val results = deserializeQuizResults(entity.quizResultsJson)
            return Pair(questions, results)
        }

        override suspend fun getRandomQuestions(
            category: String,
            type: String,
            limit: Int,
        ): List<Question> {
            return questionDao.getRandomQuestions(category, type, limit).map { entityToQuestion(it) }
        }

        override suspend fun getQuestionCountByCategory(category: String): Int {
            return questionDao.getQuestionCountByCategory(category)
        }
    }
