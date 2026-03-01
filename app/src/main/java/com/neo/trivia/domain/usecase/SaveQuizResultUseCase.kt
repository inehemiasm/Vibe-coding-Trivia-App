package com.neo.trivia.domain.usecase

import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Question
import com.neo.trivia.domain.model.QuizResult
import com.neo.trivia.domain.repository.TriviaRepository
import javax.inject.Inject

class SaveQuizResultUseCase @Inject constructor(
    private val triviaRepository: TriviaRepository
) {
    suspend fun save(
        category: Category,
        score: Int,
        totalQuestions: Int,
        questions: List<Question>,
        quizResults: List<QuizResult>
    ) {
        triviaRepository.save(
            category = category,
            score = score,
            totalQuestions = totalQuestions,
            questions = questions,
            quizResults = quizResults
        )
    }
}
