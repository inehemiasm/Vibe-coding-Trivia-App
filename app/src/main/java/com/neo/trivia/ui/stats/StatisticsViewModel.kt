package com.neo.trivia.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.trivia.domain.model.QuizHistory
import com.neo.trivia.domain.usecase.GetQuizResultsUseCase
import com.neo.trivia.domain.usecase.GetStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    getStatisticsUseCase: GetStatisticsUseCase,
    private val getQuizResultsUseCase: GetQuizResultsUseCase
) : ViewModel() {

    val quizResultsHistory: StateFlow<List<QuizHistory>> = getStatisticsUseCase.getResultsHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val totalQuestions: StateFlow<Int> = getStatisticsUseCase.getQuizResults()
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )
}
