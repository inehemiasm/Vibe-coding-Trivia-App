package com.neo.trivia.domain.model

data class CodingQuestion(
    val id: String,
    val title: String,
    val description: String,
    val solution: String,
    val difficulty: String,
    val category: String,
    val isSolved: Boolean = false
)
