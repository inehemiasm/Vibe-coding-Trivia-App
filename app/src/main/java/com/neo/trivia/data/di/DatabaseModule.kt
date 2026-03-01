package com.neo.trivia.data.di

import androidx.room.Room
import com.neo.trivia.data.database.TriviaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTriviaDatabase(app: android.app.Application): TriviaDatabase {
        return Room.databaseBuilder(
            app,
            TriviaDatabase::class.java,
            "trivia_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideQuestionDao(database: TriviaDatabase) = database.questionDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(database: TriviaDatabase) = database.favoriteDao()

    @Provides
    @Singleton
    fun provideQuizResultDao(database: TriviaDatabase) = database.quizResultDao()
}