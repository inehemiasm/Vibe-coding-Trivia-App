package com.neo.trivia.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: String,
    val question: String,
    val answers: List<String>,
    val correctAnswer: String,
    val category: String,
    val type: String
)
