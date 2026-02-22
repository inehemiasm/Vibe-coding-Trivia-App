package com.neo.trivia.data.repository

import com.neo.trivia.data.api.TriviaApi
import com.neo.trivia.data.database.dao.FavoriteDao
import com.neo.trivia.data.database.dao.QuestionDao
import com.neo.trivia.data.database.entity.FavoriteEntity
import com.neo.trivia.data.database.entity.QuestionEntity
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TriviaRepositoryImpl @Inject constructor(
    val api: TriviaApi,
    val questionDao: QuestionDao,
    val favoriteDao: FavoriteDao
) : TriviaRepository {

    override suspend fun getQuestions(amount: Int, category: Category?): Result<List<Question>> {
        return try {
            val categoryId = category?.id
            val response = api.getQuestions(amount, categoryId)
            if (response.isSuccessful && response.body() != null) {
                val questionModels = response.body()!!.results.mapIndexed { index, apiQuestion ->
                    Question(
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

    override fun getQuestionsFromCache(): Flow<List<Question>> {
        return questionDao.getAllQuestions().map { entities ->
            entities.map { entity ->
                Question(
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

    override fun searchQuestions(query: String): Flow<List<Question>> {
        return questionDao.searchQuestions(query).map { entities ->
            entities.map { entity ->
                Question(
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

    override suspend fun clearCache() {
        questionDao.clearAll()
    }

    override suspend fun toggleFavorite(question: Question): Boolean {
        val isFavorite = favoriteDao.isFavorite(question.id)
        return if (isFavorite) {
            favoriteDao.removeFavorite(question.id)
            false
        } else {
            favoriteDao.insert(FavoriteEntity(questionId = question.id))
            true
        }
    }

    override fun getFavoriteQuestions(): Flow<List<Question>> {
        return favoriteDao.getFavoriteQuestions().map { entities ->
            entities.map { entity ->
                Question(
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

    override fun getAllQuestions(): Flow<List<Question>> {
        return questionDao.getAllQuestions().map { entities ->
            entities.map { entity ->
                Question(
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