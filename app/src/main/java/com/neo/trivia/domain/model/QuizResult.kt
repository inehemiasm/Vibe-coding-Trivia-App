package com.neo.trivia.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizResult(
    val question: Question,
    val selectedAnswerIndex: Int,
    val correctAnswerIndex: Int,
    val isCorrect: Boolean
)
