package com.neo.trivia.domain.usecase

import com.neo.trivia.data.database.dao.QuizResultDao
import com.neo.trivia.data.database.entity.QuizResultEntity
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizResult
import kotlinx.coroutines.flow.count
import javax.inject.Inject

class SaveQuizResultUseCase @Inject constructor(
    private val quizResultDao: QuizResultDao
) {
    suspend fun save(
        category: Category,
        score: Int,
        totalQuestions: Int,
        questions: List<Question>,
        quizResults: List<QuizResult>
    ) {
        // Save the result
        val resultEntity = QuizResultEntity.from(
            category = category,
            score = score,
            totalQuestions = totalQuestions,
            questions = questions,
            quizResults = quizResults
        )

        // Save the result
        quizResultDao.insert(resultEntity)

        // Check current count and delete oldest if needed
        val count = quizResultDao.getRecentResults().count()
        if (count > 10) {
            quizResultDao.deleteOldest(count)
        }
    }
}