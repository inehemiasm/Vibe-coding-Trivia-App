package com.neo.trivia.ui

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * Navigation events that can be sent from any part of the app
 * Uses SharedFlow for efficient event handling
 */
sealed class NavigationEvent {
    object Back : NavigationEvent()

    object Home : NavigationEvent()

    object QuizResults : NavigationEvent()
}

/**
 * Navigation event bus for handling navigation actions
 * Provides a centralized place for navigation events
 */
class NavigationEventBus {
    private val _events = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    suspend fun send(event: NavigationEvent) {
        _events.emit(event)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun onBack() {
        // Send back event without suspending
        kotlinx.coroutines.GlobalScope.launch {
            send(NavigationEvent.Back)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun onHome() {
        kotlinx.coroutines.GlobalScope.launch {
            send(NavigationEvent.Home)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun onQuizResults() {
        kotlinx.coroutines.GlobalScope.launch {
            send(NavigationEvent.QuizResults)
        }
    }
}
