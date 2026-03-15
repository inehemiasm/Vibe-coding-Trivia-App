package com.neo.trivia.data.di

import com.neo.trivia.data.repository.TechHubRepositoryImpl
import com.neo.trivia.data.repository.TriviaRepositoryImpl
import com.neo.trivia.domain.repository.TechHubRepository
import com.neo.trivia.domain.repository.TriviaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTriviaRepository(impl: TriviaRepositoryImpl): TriviaRepository

    @Binds
    @Singleton
    abstract fun bindMediumRepository(impl: TechHubRepositoryImpl): TechHubRepository
}
