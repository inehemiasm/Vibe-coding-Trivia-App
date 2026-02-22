package com.neo.trivia.data.repository

import com.neo.trivia.data.api.Category
import com.neo.trivia.data.api.TriviaApi
import com.neo.trivia.data.database.dao.FavoriteDao
import com.neo.trivia.data.database.dao.QuestionDao
import com.neo.trivia.data.database.entity.FavoriteEntity
import com.neo.trivia.data.database.entity.QuestionEntity
import com.neo.trivia.data.model.QuestionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TriviaRepository @Inject constructor(
    private val api: TriviaApi,
    private val questionDao: QuestionDao,
    private val favoriteDao: FavoriteDao
) {
    suspend fun getQuestions(amount: Int, category: Category? = null): Result<List<QuestionModel>> {
        return try {
            val categoryId = category?.id
            val response = api.getQuestions(amount, categoryId)
            if (response.isSuccessful && response.body() != null) {
                val questionModels = response.body()!!.results.mapIndexed { index, apiQuestion ->
                    QuestionModel(
                        id = "q_${System.currentTimeMillis()}_$index",
                        question = apiQuestion.question,
                        correctAnswer = apiQuestion.correctAnswer,
                        incorrectAnswers = apiQuestion.incorrectAnswers,
                        category = apiQuestion.category,
                        type = apiQuestion.type
                    )
                }

                val questionEntities = questionModels.map { model ->
                    QuestionEntity(
                        id = model.id,
                        question = model.question,
                        correctAnswer = model.correctAnswer,
                        incorrectAnswers = model.incorrectAnswers,
                        category = model.category,
                        type = model.type
                    )
                }
                questionDao.insertAll(questionEntities)

                Result.success(questionModels)
            } else {
                Result.failure(Exception("Failed to fetch questions"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getQuestionsFromCache(): Flow<List<QuestionModel>> {
        return questionDao.getAllQuestions().map { entities ->
            entities.map { entity ->
                QuestionModel(
                    id = entity.id,
                    question = entity.question,
                    correctAnswer = entity.correctAnswer,
                    incorrectAnswers = entity.incorrectAnswers,
                    category = entity.category,
                    type = entity.type
                )
            }
        }
    }

    fun searchQuestions(query: String): Flow<List<QuestionModel>> {
        return questionDao.searchQuestions(query).map { entities ->
            entities.map { entity ->
                QuestionModel(
                    id = entity.id,
                    question = entity.question,
                    correctAnswer = entity.correctAnswer,
                    incorrectAnswers = entity.incorrectAnswers,
                    category = entity.category,
                    type = entity.type
                )
            }
        }
    }

    suspend fun clearCache() {
        questionDao.clearAll()
    }

    suspend fun toggleFavorite(question: QuestionModel): Boolean {
        val isFavorite = favoriteDao.isFavorite(question.id)
        return if (isFavorite) {
            favoriteDao.removeFavorite(question.id)
            false
        } else {
            favoriteDao.insert(FavoriteEntity(questionId = question.id))
            true
        }
    }

    fun getFavoriteQuestions(): Flow<List<QuestionModel>> {
        return favoriteDao.getFavoriteQuestions().map { entities ->
            entities.map { entity ->
                QuestionModel(
                    id = entity.id,
                    question = entity.question,
                    correctAnswer = entity.correctAnswer,
                    incorrectAnswers = entity.incorrectAnswers,
                    category = entity.category,
                    type = entity.type
                )
            }
        }
    }

    fun getAllQuestions(): Flow<List<QuestionModel>> {
        return questionDao.getAllQuestions().map { entities ->
            entities.map { entity ->
                QuestionModel(
                    id = entity.id,
                    question = entity.question,
                    correctAnswer = entity.correctAnswer,
                    incorrectAnswers = entity.incorrectAnswers,
                    category = entity.category,
                    type = entity.type
                )
            }
        }
    }
}
