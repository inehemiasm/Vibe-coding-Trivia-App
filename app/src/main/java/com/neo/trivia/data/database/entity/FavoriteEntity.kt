package com.neo.trivia.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a favorite question.
 * Stored in the 'favorites' table in the local database.
 * This entity maintains a many-to-one relationship with QuestionEntity.
 *
 * @property questionId The unique ID of the question in favorites
 */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val questionId: String
)