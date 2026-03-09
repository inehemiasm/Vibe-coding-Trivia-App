package com.neo.trivia.util

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Utility class for cleaning HTML entities from text
 */
object HtmlTextCleaner {
    /**
     * Cleans HTML entities from text including:
     * - HTML tags
     * - HTML entities like &amp;, &lt;, &gt;, &quot;
     * - URL encoded characters
     * - Extra whitespace
     */
    fun cleanHtmlText(text: String?): String {
        if (text.isNullOrEmpty()) {
            return ""
        }

        var cleanText = text

        // Remove HTML tags
        cleanText = cleanText.replace(Regex("<[^>]*>"), "")

        // Decode HTML entities
        cleanText = cleanText.replace("&amp;", "&")
        cleanText = cleanText.replace("&lt;", "<")
        cleanText = cleanText.replace("&gt;", ">")
        cleanText = cleanText.replace("&quot;", "\"")
        cleanText = cleanText.replace("&apos;", "'")
        cleanText = cleanText.replace("&nbsp;", " ")

        // Handle URL encoded characters
        try {
            cleanText = URLDecoder.decode(cleanText, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            // If URL decoding fails, continue with existing text
        }

        // Trim and normalize whitespace
        cleanText = cleanText.trim()
        cleanText = cleanText.replace(Regex("\\s+"), " ")

        return cleanText
    }

    /**
     * Cleans a list of text items (for answers, etc.)
     */
    fun cleanTextList(textList: List<String>): List<String> {
        return textList.map { cleanHtmlText(it) }
    }
}
