package com.neo.trivia.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.QuotaExceededException
import com.neo.trivia.BuildConfig
import com.neo.trivia.data.preferences.AiPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TriviaAiManager @Inject constructor(
    private val aiPreferencesManager: AiPreferencesManager
) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash-lite",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    val isQuotaExceeded: Flow<Boolean> = aiPreferencesManager.isQuotaExceeded

    suspend fun generateHint(question: String, answers: List<String>): String? {
        if (isQuotaExceeded.first()) return null

        val prompt = "Provide a very short, subtle hint for this trivia question without giving away the answer. " +
                "Question: $question. Options: ${answers.joinToString(", ")}"
        
        return try {
            val response = generativeModel.generateContent(prompt)
            response.text?.trim()
        } catch (e: QuotaExceededException) {
            handleQuotaExceeded(e)
            null
        } catch (e: Exception) {
            Timber.e(e, "Error generating hint")
            null
        }
    }

    suspend fun generateExplanation(question: String, correctAnswer: String): String? {
        if (isQuotaExceeded.first()) return null

        val prompt = "Briefly explain why '$correctAnswer' is the correct answer to the question: '$question'. " +
                "Keep it under 2 sentences."
        
        return try {
            val response = generativeModel.generateContent(prompt)
            response.text?.trim()
        } catch (e: QuotaExceededException) {
            handleQuotaExceeded(e)
            null
        } catch (e: Exception) {
            Timber.e(e, "Error generating explanation")
            null
        }
    }

    suspend fun generateImageKeyword(question: String): String? {
        if (isQuotaExceeded.first()) return null

        val prompt = "Based on this trivia question, provide a single, highly visual search keyword or phrase (max 3 words) that would return a great related image. " +
                "Just the keyword, nothing else. Question: $question"
        
        return try {
            val response = generativeModel.generateContent(prompt)
            response.text?.trim()?.removeSurrounding("\"")
        } catch (e: QuotaExceededException) {
            handleQuotaExceeded(e)
            null
        } catch (e: Exception) {
            Timber.e(e, "Error generating image keyword")
            null
        }
    }

    private suspend fun handleQuotaExceeded(e: QuotaExceededException) {
        val message = e.message ?: ""
        Timber.w("Gemini quota exceeded. Disabling AI features. Error: $message")
        val retryAfterMs = parseRetryAfterMs(message)
        
        aiPreferencesManager.setQuotaExceeded(true, retryAfterMs)
    }

    /**
     * Attempts to parse a retry duration from the error message.
     * Example: "Please retry in 21.560440833s"
     */
    private fun parseRetryAfterMs(message: String): Long? {
        return try {
            // Regex to find numbers followed by 's' (seconds)
            val regex = """retry in ([\d.]+)s""".toRegex()
            val match = regex.find(message)
            val seconds = match?.groupValues?.get(1)?.toDoubleOrNull()
            
            if (seconds != null) {
                // Add a small buffer (e.g., 1 second) to be safe
                ((seconds + 1.0) * 1000).toLong()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
