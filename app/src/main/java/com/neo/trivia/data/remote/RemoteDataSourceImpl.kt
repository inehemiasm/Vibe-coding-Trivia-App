package com.neo.trivia.data.remote

import com.neo.trivia.data.Result
import com.neo.trivia.data.api.TriviaApi
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import com.neo.trivia.domain.model.Question as DomainQuestion

/**
 * Remote data source implementation using Retrofit API.
 * Handles all API communication with Open Trivia DB.
 * Converts API responses to domain Question models.
 *
 * This implementation handles:
 * - Question fetching with category and difficulty filtering
 * - Category listing
 * - Error handling with Result wrapper
 * - Async operations using Flow and suspend functions
 */
@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val api: TriviaApi
) : RemoteDataSource {

    /**
     * Converts API Question object to domain Question object.
     * Includes the correct answer and all incorrect answers as possible choices.
     */
    override fun getQuestions(
        amount: Int,
        category: Category?,
        difficulty: Difficulty
    ): Flow<Result<List<DomainQuestion>>> {
        return flow {
            try {
                emit(Result.Loading())

                val categoryId = category?.id
                val response = api.getQuestions(amount, categoryId, difficulty.name.lowercase())

                if (response.isSuccessful && response.body() != null) {
                    val questions = response.body()!!.results.mapIndexed { index, apiQuestion ->
                        DomainQuestion(
                            id = index.toString(),
                            question = apiQuestion.question,
                            answers = listOf(apiQuestion.correctAnswer, *apiQuestion.incorrectAnswers.toTypedArray()),
                            correctAnswer = apiQuestion.correctAnswer,
                            category = apiQuestion.category,
                            type = apiQuestion.type
                        )
                    }
                    emit(Result.Success(questions))
                } else {
                    emit(Result.Failure(Exception("Failed to fetch questions: ${response.code()}")))
                }
            } catch (e: Exception) {
                emit(Result.Failure(e))
            }
        }
    }

    override suspend fun getQuestionsSync(
        amount: Int,
        category: Category?,
        difficulty: Difficulty
    ): Result<List<DomainQuestion>> {
        return try {
            val categoryId = category?.id
            val response = api.getQuestions(amount, categoryId, difficulty.name.lowercase())

            if (response.isSuccessful && response.body() != null) {
                val questions = response.body()!!.results.mapIndexed { index, apiQuestion ->
                    DomainQuestion(
                        id = index.toString(),
                        question = apiQuestion.question,
                        answers = listOf(apiQuestion.correctAnswer, *apiQuestion.incorrectAnswers.toTypedArray()),
                        correctAnswer = apiQuestion.correctAnswer,
                        category = apiQuestion.category,
                        type = apiQuestion.type
                    )
                }
                Result.Success(questions)
            } else {
                Result.Failure(Exception("Failed to fetch questions: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = api.getCategories()
            if (response.isSuccessful && response.body() != null) {
                val categories = response.body()!!.triviaCategories.map { apiCategory ->
                    Category(id = apiCategory.id, name = apiCategory.name)
                }
                Result.Success(categories)
            } else {
                Result.Failure(Exception("Failed to fetch categories: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    companion object {
        private const val QUESTION_TYPE = "multiple"
    }
}