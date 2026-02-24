package com.neo.trivia.domain.di

import com.neo.trivia.data.database.TriviaDatabase
import com.neo.trivia.data.database.dao.QuizHistoryDao
import com.neo.trivia.domain.repository.TriviaRepository
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
    fun provideGetQuestionsUseCase(repository: TriviaRepository): com.neo.trivia.domain.usecase.GetQuestionsUseCase {
        return com.neo.trivia.domain.usecase.GetQuestionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavoriteQuestionsUseCase(repository: TriviaRepository): com.neo.trivia.domain.usecase.GetFavoriteQuestionsUseCase {
        return com.neo.trivia.domain.usecase.GetFavoriteQuestionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(repository: TriviaRepository): com.neo.trivia.domain.usecase.ToggleFavoriteUseCase {
        return com.neo.trivia.domain.usecase.ToggleFavoriteUseCase(repository)
    }

    @Provides
    @Singleton
    fun quizHistoryDao(database: TriviaDatabase): QuizHistoryDao {
        return database.quizHistoryDao()
    }
}
