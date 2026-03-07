package com.neo.trivia.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a trivia category.
 * Stored in the 'categories' table in the local database.
 *
 * @property id Unique identifier for the category
 * @property name The name of the category
 * @property icon The icon associated with the category
 * @property createdAt Timestamp when the category was created
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val icon: String,
    val createdAt: Long = System.currentTimeMillis()
)