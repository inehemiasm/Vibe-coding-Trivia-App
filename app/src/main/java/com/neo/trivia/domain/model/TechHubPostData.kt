package com.neo.trivia.domain.model

data class TechHubPostData(
    val id: String,
    val title: String,
    val author: String,
    val content: String,
    val imageUrl: String?,
    val url: String,
    val isSaved: Boolean = false
)
