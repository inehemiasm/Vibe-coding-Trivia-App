package com.neo.trivia.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a LeetCode-style coding question.
 *
 * @property id Unique identifier
 * @property title Title of the problem
 * @property description Detailed problem description
 * @property solution Example solution/explanation
 * @property difficulty Easy, Medium, or Hard
 * @property category Topic category (e.g., Arrays, Dynamic Programming)
 * @property isSolved Whether the user has marked it as solved
 * @property createdAt Timestamp of entry
 */
@Entity(tableName = "coding_questions")
data class CodingQuestionEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val solution: String,
    val difficulty: String,
    val category: String,
    val isSolved: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
