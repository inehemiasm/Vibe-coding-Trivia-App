package com.neo.trivia.data.database.entity

import androidx.room.Entity

/**
 * Room entity representing a quiz history entry.
 * Stored in the 'quiz_history' table in the local database.
 *
 * @property categoryName The name of the category
 * @property quizDate The date of the quiz (formatted as string)
 * @property score The user's score
 * @property totalQuestions Total number of questions in the quiz
 * @property questionsJson JSON serialized questions
 * @property quizResultsJson JSON serialized quiz results
 * @property createdAt Timestamp when the entry was created
 */
@Entity(
    tableName = "quiz_history",
    primaryKeys = ["categoryName", "quizDate"],
)
data class QuizHistoryEntity(
    val categoryName: String,
    val quizDate: String,
    val score: Int,
    val totalQuestions: Int,
    val questionsJson: String,
    val quizResultsJson: String,
    val createdAt: Long = System.currentTimeMillis(),
)
