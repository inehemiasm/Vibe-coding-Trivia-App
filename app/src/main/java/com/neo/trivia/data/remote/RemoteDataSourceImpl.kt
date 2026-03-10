package com.neo.trivia.data.remote

import com.neo.trivia.data.Result
import com.neo.trivia.data.api.TriviaApi
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.util.HtmlTextCleaner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import com.neo.trivia.domain.model.Question as DomainQuestion

/**
 * Remote data source implementation using Retrofit API.
 */
@Singleton
class RemoteDataSourceImpl
    @Inject
    constructor(
        private val api: TriviaApi,
    ) : RemoteDataSource {

        override fun getQuestions(
            amount: Int,
            category: Category?,
            difficulty: Difficulty,
        ): Flow<Result<List<DomainQuestion>>> {
            return flow {
                try {
                    emit(Result.Loading())

                    val categoryId = category?.id
                    val response = api.getQuestions(amount, categoryId, difficulty.name.lowercase())

                    if (response.isSuccessful && response.body() != null) {
                        val questions =
                            response.body()!!.results.map { apiQuestion ->
                                DomainQuestion(
                                    // Use a hash of the question text to create a unique ID 
                                    // and avoid collisions/overwriting in the database.
                                    id = apiQuestion.question.hashCode().toString(),
                                    question = HtmlTextCleaner.cleanHtmlText(apiQuestion.question),
                                    answers =
                                        HtmlTextCleaner.cleanTextList(
                                            listOf(apiQuestion.correctAnswer, *apiQuestion.incorrectAnswers.toTypedArray()),
                                        ).shuffled(),
                                    correctAnswer = HtmlTextCleaner.cleanHtmlText(apiQuestion.correctAnswer),
                                    category = HtmlTextCleaner.cleanHtmlText(apiQuestion.category),
                                    type = HtmlTextCleaner.cleanHtmlText(apiQuestion.type),
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
            difficulty: Difficulty,
        ): Result<List<DomainQuestion>> {
            return try {
                val categoryId = category?.id
                val response = api.getQuestions(amount, categoryId, difficulty.name.lowercase())

                if (response.isSuccessful && response.body() != null) {
                    val questions =
                        response.body()!!.results.map { apiQuestion ->
                            DomainQuestion(
                                id = apiQuestion.question.hashCode().toString(),
                                question = HtmlTextCleaner.cleanHtmlText(apiQuestion.question),
                                answers =
                                    HtmlTextCleaner.cleanTextList(
                                        listOf(apiQuestion.correctAnswer, *apiQuestion.incorrectAnswers.toTypedArray()),
                                    ).shuffled(),
                                correctAnswer = HtmlTextCleaner.cleanHtmlText(apiQuestion.correctAnswer),
                                category = HtmlTextCleaner.cleanHtmlText(apiQuestion.category),
                                type = HtmlTextCleaner.cleanHtmlText(apiQuestion.type),
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
                    val categories =
                        response.body()!!.triviaCategories.map { apiCategory ->
                            Category(id = apiCategory.id, name = HtmlTextCleaner.cleanHtmlText(apiCategory.name))
                        }
                    Result.Success(categories)
                } else {
                    Result.Failure(Exception("Failed to fetch categories: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.Failure(e)
            }
        }
    }
