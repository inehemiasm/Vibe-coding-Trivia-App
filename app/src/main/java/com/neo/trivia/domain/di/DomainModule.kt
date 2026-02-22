package com.neo.trivia.domain.di

import com.neo.trivia.data.di.DataModule
import com.neo.trivia.domain.repository.TriviaRepository
import com.neo.trivia.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetQuestionsUseCase(repository: TriviaRepository): GetQuestionsUseCase {
        return GetQuestionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetStatisticsUseCase(repository: TriviaRepository): GetStatisticsUseCase {
        return GetStatisticsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavoriteQuestionsUseCase(repository: TriviaRepository): GetFavoriteQuestionsUseCase {
        return GetFavoriteQuestionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(repository: TriviaRepository): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideClearHistoryUseCase(repository: TriviaRepository): ClearHistoryUseCase {
        return ClearHistoryUseCase(repository)
    }
}