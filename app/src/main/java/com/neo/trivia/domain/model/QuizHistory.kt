package com.neo.trivia.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizHistory(
    val id: String,
    val categoryName: String,
    val quizDate: String,
    val score: Int,
    val totalQuestions: Int,
    val categoryIcon: String,
    val accuracy: Double,
) {
    companion object {
        fun fromQuizResultEntity(quizResultEntity: com.neo.trivia.data.database.entity.QuizResultEntity): QuizHistory {
            return QuizHistory(
                id = quizResultEntity.id,
                categoryName = quizResultEntity.categoryName,
                quizDate =
                    java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                        .format(java.util.Date(quizResultEntity.timestamp)),
                score = quizResultEntity.score,
                totalQuestions = quizResultEntity.totalQuestions,
                categoryIcon = quizResultEntity.categoryIcon,
                accuracy =
                    if (quizResultEntity.totalQuestions > 0) {
                        (quizResultEntity.score.toDouble() / quizResultEntity.totalQuestions.toDouble()) * 100
                    } else {
                        0.0
                    },
            )
        }
    }
}
