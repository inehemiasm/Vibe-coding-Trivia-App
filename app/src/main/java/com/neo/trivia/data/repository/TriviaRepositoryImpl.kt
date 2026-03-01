package com.neo.trivia.data.repository

import com.neo.trivia.data.local.LocalDataSource
import com.neo.trivia.data.remote.RemoteDataSource
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Difficulty
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TriviaRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : TriviaRepository {

    override suspend fun getQuestions(
        amount: Int, 
        category: Category?,
        difficulty: Difficulty
    ): kotlin.Result<List<Question>> {
        return try {
            when (val remoteResult = remoteDataSource.getQuestionsSync(amount, category, difficulty)) {
                is com.neo.trivia.data.Result.Success -> {
                    localDataSource.insertQuestions(remoteResult.data)
                    kotlin.Result.success(remoteResult.data)
                }
                is com.neo.trivia.data.Result.Failure -> kotlin.Result.failure(remoteResult.error)
                is com.neo.trivia.data.Result.Loading -> kotlin.Result.failure(IllegalStateException("getQuestionsSync should not be in a loading state"))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }

    override suspend fun getCategories(): kotlin.Result<List<Category>> {
        return try {
            when (val result = remoteDataSource.getCategories()) {
                is com.neo.trivia.data.Result.Success -> kotlin.Result.success(result.data)
                is com.neo.trivia.data.Result.Failure -> kotlin.Result.failure(result.error)
                is com.neo.trivia.data.Result.Loading -> kotlin.Result.failure(IllegalStateException("getCategories should not be in a loading state"))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(e)
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
        return localDataSource.toggleFavorite(question.question)
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
        quizResults: List<QuizResult>
    ) {
        localDataSource.saveQuizResult(
            category = category,
            score = score,
            totalQuestions = totalQuestions,
            questions = questions,
            quizResults = quizResults
        )
    }

    override fun getQuizResults(): Flow<List<QuizResult>> {
        return localDataSource.getQuizResults()
    }
}
