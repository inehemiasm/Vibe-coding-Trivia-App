package com.neo.trivia.data.repository

import com.neo.trivia.core.NetworkMonitor
import com.neo.trivia.data.local.LocalDataSource
import com.neo.trivia.data.remote.RemoteDataSource
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizHistory
import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TriviaRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: RemoteDataSource,
        private val localDataSource: LocalDataSource,
        private val networkMonitor: NetworkMonitor,
    ) : TriviaRepository {
        override suspend fun getQuestions(
            amount: Int,
            category: Category?,
            difficulty: Difficulty,
        ): kotlin.Result<List<Question>> {
            // If offline, bypass remote and go straight to local
            if (!networkMonitor.isOnline.first()) {
                if (category != null) {
                    val offlineQuestions = localDataSource.getRandomQuestionsWithFallback(
                        category = category.name,
                        limit = amount
                    )
                    if (offlineQuestions.isNotEmpty()) {
                        return kotlin.Result.success(offlineQuestions)
                    }
                }
                return kotlin.Result.failure(Exception("Offline and no local data found"))
            }

            return try {
                when (val remoteResult = remoteDataSource.getQuestionsSync(amount, category, difficulty)) {
                    is com.neo.trivia.data.Result.Success -> {
                        localDataSource.insertQuestions(remoteResult.data)
                        kotlin.Result.success(remoteResult.data)
                    }
                    is com.neo.trivia.data.Result.Failure -> {
                        // Fallback to local if remote fails
                        if (category != null) {
                            val offlineQuestions = localDataSource.getRandomQuestionsWithFallback(
                                category = category.name,
                                limit = amount
                            )
                            if (offlineQuestions.size >= amount) {
                                return kotlin.Result.success(offlineQuestions)
                            }
                        }
                        kotlin.Result.failure(remoteResult.error)
                    }
                    is com.neo.trivia.data.Result.Loading ->
                        kotlin.Result.failure(
                            IllegalStateException("getQuestionsSync should not be in a loading state"),
                        )
                }
            } catch (e: Exception) {
                // Fallback to local on exception
                if (category != null) {
                    val offlineQuestions = localDataSource.getRandomQuestionsWithFallback(
                        category = category.name,
                        limit = amount
                    )
                    if (offlineQuestions.size >= amount) {
                        return kotlin.Result.success(offlineQuestions)
                    }
                }
                kotlin.Result.failure(e)
            }
        }

        override suspend fun getCategories(): kotlin.Result<List<Category>> {
            if (!networkMonitor.isOnline.first()) {
                val cached = localDataSource.getCachedCategories().first()
                return if (cached.isNotEmpty()) {
                    kotlin.Result.success(cached)
                } else {
                    kotlin.Result.failure(Exception("Offline and no cached categories"))
                }
            }

            return try {
                when (val result = remoteDataSource.getCategories()) {
                    is com.neo.trivia.data.Result.Success -> {
                        localDataSource.insertCategories(result.data)
                        kotlin.Result.success(result.data)
                    }
                    is com.neo.trivia.data.Result.Failure -> {
                        val cached = localDataSource.getCachedCategories().first()
                        if (cached.isNotEmpty()) {
                            kotlin.Result.success(cached)
                        } else {
                            kotlin.Result.failure(result.error)
                        }
                    }
                    is com.neo.trivia.data.Result.Loading ->
                        kotlin.Result.failure(
                            IllegalStateException("getCategories should not be in a loading state"),
                        )
                }
            } catch (e: Exception) {
                val cached = localDataSource.getCachedCategories().first()
                if (cached.isNotEmpty()) {
                    kotlin.Result.success(cached)
                } else {
                    kotlin.Result.failure(e)
                }
            }
        }

        override fun getQuestionsFromCache(): Flow<List<Question>> {
            return localDataSource.getAllQuestions()
        }

        override fun searchQuestions(query: String): Flow<List<Question>> {
            return localDataSource.searchQuestions(query)
        }

        override suspend fun clearCache() {
            localDataSource.clearCache()
        }

        override suspend fun toggleFavorite(question: Question): Boolean {
            return localDataSource.toggleFavorite(question.id)
        }

        override fun getFavoriteQuestions(): Flow<List<Question>> {
            return localDataSource.getFavoriteQuestions()
        }

        override fun getAllQuestions(): Flow<List<Question>> {
            return localDataSource.getAllQuestions()
        }

        override suspend fun save(
            category: Category,
            score: Int,
            totalQuestions: Int,
            questions: List<Question>,
            quizResults: List<QuizResult>,
        ) {
            localDataSource.saveQuizResult(
                category = category,
                score = score,
                totalQuestions = totalQuestions,
                questions = questions,
                quizResults = quizResults,
            )
        }

        override fun getQuizResults(): Flow<List<QuizResult>> {
            return localDataSource.getQuizResults()
        }

        override fun getQuizHistory(): Flow<List<QuizHistory>> {
            return localDataSource.getQuizHistory()
        }

        override fun getLatestQuizResult(): Flow<QuizResult?> {
            return localDataSource.getLatestQuizResult()
        }

        override suspend fun getQuizResultById(id: String): Pair<List<Question>, List<QuizResult>>? {
            return localDataSource.getQuizResultById(id)
        }

        override suspend fun getQuestionsOffline(
            category: Category,
            difficulty: Difficulty,
            amount: Int,
        ): List<Question> {
            return localDataSource.getRandomQuestionsWithFallback(
                category = category.name,
                limit = amount
            )
        }

        override suspend fun syncQuestions(categories: List<Category>, targetAmountPerCategory: Int) {
            if (!networkMonitor.isOnline.first()) return

            for (category in categories) {
                val currentCount = localDataSource.getQuestionCountByCategory(category.name)
                if (currentCount < targetAmountPerCategory) {
                    val amountToFetch = targetAmountPerCategory - currentCount
                    for (difficulty in Difficulty.entries) {
                        val result = remoteDataSource.getQuestionsSync(
                            amount = amountToFetch / 3 + 1,
                            category = category,
                            difficulty = difficulty
                        )
                        if (result is com.neo.trivia.data.Result.Success) {
                            localDataSource.insertQuestions(result.data)
                        }
                    }
                }
            }
        }

        override fun getCategoriesWithQuestions(): Flow<List<Category>> {
            return localDataSource.getCategoriesWithQuestions()
        }
    }
