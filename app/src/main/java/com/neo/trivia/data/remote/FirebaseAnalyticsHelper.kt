package com.neo.trivia.data.remote

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.neo.trivia.domain.analytics.AnalyticsEvent
import com.neo.trivia.domain.analytics.AnalyticsHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsHelper {

    override fun logEvent(event: AnalyticsEvent) {
        val bundle = Bundle().apply {
            event.extras.forEach { parameter ->
                putString(parameter.key, parameter.value)
            }
        }
        firebaseAnalytics.logEvent(event.type, bundle)
    }

    override fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
    }
}
