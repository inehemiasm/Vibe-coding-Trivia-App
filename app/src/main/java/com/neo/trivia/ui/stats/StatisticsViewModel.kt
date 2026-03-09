package com.neo.trivia.ui.stats

import androidx.lifecycle.viewModelScope
import com.neo.trivia.core.BaseViewModel
import com.neo.trivia.core.UiEffect
import com.neo.trivia.core.UiIntent
import com.neo.trivia.core.UiState
import com.neo.trivia.domain.model.QuizHistory
import com.neo.trivia.domain.usecase.GetQuizResultsUseCase
import com.neo.trivia.domain.usecase.GetStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel
    @Inject
    constructor(
        private val getStatisticsUseCase: GetStatisticsUseCase,
        private val getQuizResultsUseCase: GetQuizResultsUseCase,
    ) : BaseViewModel<StatisticsUiState, StatisticsIntent, StatisticsUiEffect>(StatisticsUiState()) {
        init {
            onIntent(StatisticsIntent.LoadStatistics)
        }

        override suspend fun handleIntent(intent: StatisticsIntent) {
            when (intent) {
                is StatisticsIntent.LoadStatistics -> loadStatistics()
            }
        }

        private fun loadStatistics() {
            viewModelScope.launch {
                combine(
                    getStatisticsUseCase.getResultsHistory(),
                    getStatisticsUseCase.getQuizResults(),
                ) { history, results ->
                    Pair(history, results.size)
                }.collectLatest { (history, total) ->
                    setState {
                        copy(
                            quizResultsHistory = history,
                            totalQuestions = total,
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

data class StatisticsUiState(
    val quizResultsHistory: List<QuizHistory> = emptyList(),
    val totalQuestions: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null,
) : UiState

sealed class StatisticsIntent : UiIntent {
    object LoadStatistics : StatisticsIntent()
}

sealed class StatisticsUiEffect : UiEffect
