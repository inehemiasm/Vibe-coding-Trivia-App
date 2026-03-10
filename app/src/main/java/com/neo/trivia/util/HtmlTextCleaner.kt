package com.neo.trivia.util

import android.text.Html

/**
 * Utility class for cleaning HTML entities from text
 */
object HtmlTextCleaner {
    /**
     * Cleans HTML entities from text including:
     * - HTML tags
     * - HTML entities like &amp;, &lt;, &gt;, &quot;, &#039;
     * - Extra whitespace
     */
    fun cleanHtmlText(text: String?): String {
        if (text.isNullOrEmpty()) {
            return ""
        }

        // Use Android's Html.fromHtml to handle all HTML entities correctly.
        // Since minSdk is 24, we can use the N+ version directly.
        val decodedText = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()

        // Trim and normalize whitespace
        return decodedText.trim().replace(Regex("\\s+"), " ")
    }

    /**
     * Cleans a list of text items (for answers, etc.)
     */
    fun cleanTextList(textList: List<String>): List<String> {
        return textList.map { cleanHtmlText(it) }
    }
}
