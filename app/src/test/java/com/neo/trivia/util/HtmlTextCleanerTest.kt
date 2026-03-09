package com.neo.trivia.util

import org.junit.Assert.assertEquals
import org.junit.Test

class HtmlTextCleanerTest {
    @Test
    fun testCleanHtmlTextWithHtmlTags() {
        val input = "<p>This is a <b>test</b> question</p>"
        val expected = "This is a test question"
        val result = HtmlTextCleaner.cleanHtmlText(input)
        assertEquals(expected, result)
    }

    @Test
    fun testCleanHtmlTextWithHtmlEntities() {
        val input = "What is &lt;html&gt;? &amp; more text"
        val expected = "What is <html>? & more text"
        val result = HtmlTextCleaner.cleanHtmlText(input)
        assertEquals(expected, result)
    }

    @Test
    fun testCleanHtmlTextWithMixedContent() {
        val input = "<div>Question with &quot;quotes&quot; and &lt;tags&gt;</div>"
        val expected = "Question with \"quotes\" and <tags>"
        val result = HtmlTextCleaner.cleanHtmlText(input)
        assertEquals(expected, result)
    }

    @Test
    fun testCleanHtmlTextWithEmptyString() {
        val input = ""
        val expected = ""
        val result = HtmlTextCleaner.cleanHtmlText(input)
        assertEquals(expected, result)
    }

    @Test
    fun testCleanHtmlTextWithNull() {
        val input: String? = null
        val expected = ""
        val result = HtmlTextCleaner.cleanHtmlText(input)
        assertEquals(expected, result)
    }

    @Test
    fun testCleanTextList() {
        val input = listOf("<p>Answer 1</p>", "Answer &amp; 2", "<b>Answer 3</b>")
        val expected = listOf("Answer 1", "Answer & 2", "Answer 3")
        val result = HtmlTextCleaner.cleanTextList(input)
        assertEquals(expected, result)
    }
}
