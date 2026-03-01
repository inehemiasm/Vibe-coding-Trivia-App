package com.neo.trivia.data.di

import com.neo.trivia.data.repository.TriviaRepositoryImpl
import com.neo.trivia.domain.repository.TriviaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TriviaRepositoryModule {

    @Binds
    abstract fun bindTriviaRepository(impl: TriviaRepositoryImpl): TriviaRepository
}