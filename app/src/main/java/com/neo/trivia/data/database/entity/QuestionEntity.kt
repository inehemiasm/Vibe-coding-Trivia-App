package com.neo.trivia.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a trivia question.
 * Stored in the 'questions' table in the local database.
 *
 * @property id Unique identifier for the question
 * @property question The question text
 * @property correctAnswer The correct answer to the question
 * @property incorrectAnswers List of incorrect answer choices
 * @property category The category of the question
 * @property type The type of question (e.g., multiple choice)
 * @property createdAt Timestamp when the question was cached
 */
@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey
    val id: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val category: String,
    val type: String,
    val createdAt: Long = System.currentTimeMillis()
)