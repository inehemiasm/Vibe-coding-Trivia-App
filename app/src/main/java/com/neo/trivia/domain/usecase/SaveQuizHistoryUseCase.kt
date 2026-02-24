package com.neo.trivia.domain.usecase

import com.neo.trivia.data.database.dao.QuizHistoryDao
import com.neo.trivia.data.database.entity.QuizHistoryEntity
import javax.inject.Inject

class SaveQuizHistoryUseCase @Inject constructor(
    private val quizHistoryDao: QuizHistoryDao
) {
    suspend operator fun invoke(history: QuizHistoryEntity): Long {
        return quizHistoryDao.insert(history)
    }
}