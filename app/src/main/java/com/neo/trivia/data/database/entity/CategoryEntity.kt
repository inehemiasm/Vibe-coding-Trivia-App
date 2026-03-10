package com.neo.trivia.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a trivia category.
 *
 * @property id Unique identifier for the category
 * @property name The name of the category
 * @property icon The icon name for the category
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val icon: String = "Star",
)
