package com.neo.trivia.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.neo.trivia.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TriviaAiManager @Inject constructor() {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash-lite",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun generateHint(question: String, answers: List<String>): String? {
        val prompt = "Provide a very short, subtle hint for this trivia question without giving away the answer. " +
                "Question: $question. Options: ${answers.joinToString(", ")}"
        
        return try {
            val response = generativeModel.generateContent(prompt)
            response.text?.trim()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun generateExplanation(question: String, correctAnswer: String): String? {
        val prompt = "Briefly explain why '$correctAnswer' is the correct answer to the question: '$question'. " +
                "Keep it under 2 sentences."
        
        return try {
            val response = generativeModel.generateContent(prompt)
            response.text?.trim()
        } catch (e: Exception) {
            null
        }
    }
}
