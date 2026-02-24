package com.neo.trivia.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_history")
data class QuizHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val categoryName: String?,
    val category: Int,
    val timestamp: Long
)