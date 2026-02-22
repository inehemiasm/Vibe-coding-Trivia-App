package com.neo.trivia.data.model

data class QuestionModel(
    val id: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val category: String,
    val type: String
)