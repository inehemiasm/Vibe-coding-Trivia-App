package com.neo.trivia.domain.model

data class Question(
    val id: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val category: String,
    val type: String,
    val isFavorite: Boolean = false
)