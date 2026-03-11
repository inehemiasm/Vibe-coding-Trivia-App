package com.neo.trivia.domain.analytics

interface AnalyticsHelper {
    fun logEvent(event: AnalyticsEvent)
    fun setUserProperty(name: String, value: String)
}

data class AnalyticsEvent(
    val type: String,
    val extras: List<AnalyticsParameter> = emptyList()
)

data class AnalyticsParameter(
    val key: String,
    val value: String
)
