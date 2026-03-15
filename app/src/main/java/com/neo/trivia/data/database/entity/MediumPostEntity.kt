package com.neo.trivia.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medium_posts")
data class MediumPostEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val content: String,
    val imageUrl: String?,
    val url: String,
    val savedAt: Long = System.currentTimeMillis()
)
