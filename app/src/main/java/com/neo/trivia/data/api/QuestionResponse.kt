package com.neo.trivia.data.api

import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val results: List<QuestionModel>
)

data class QuestionModel(
    @SerializedName("question")
    val question: String,
    @SerializedName("correct_answer")
    val correctAnswer: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>,
    @SerializedName("category")
    val category: String,
    @SerializedName("type")
    val type: String
) {
    val allAnswers: List<String>
        get() = (incorrectAnswers + correctAnswer).shuffled()
}