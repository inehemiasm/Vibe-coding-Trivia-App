package com.neo.trivia.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.neo.trivia.domain.model.Category
import com.neo.trivia.domain.model.Question
import java.util.UUID

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val categoryName: String,
    val categoryIcon: String,
    val score: Int,
    val totalQuestions: Int,
    val questionsJson: String,
    val quizResultsJson: String
) {
    companion object {
        fun from(
            category: Category,
            score: Int,
            totalQuestions: Int,
            questions: List<Question>,
            quizResults: List<com.neo.trivia.domain.model.QuizResult>
        ): QuizResultEntity {
            return QuizResultEntity(
                categoryName = category.name,
                categoryIcon = category.icon,
                score = score,
                totalQuestions = totalQuestions,
                questionsJson = com.google.gson.Gson().toJson(questions),
                quizResultsJson = com.google.gson.Gson().toJson(quizResults)
            )
        }
    }
}