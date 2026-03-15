package com.neo.trivia.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.neo.trivia.data.api.TriviaApi
import com.neo.trivia.data.database.TriviaDatabase
import com.neo.trivia.data.local.LocalDataSource
import com.neo.trivia.data.local.LocalDataSourceImpl
import com.neo.trivia.data.remote.RemoteDataSource
import com.neo.trivia.data.remote.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideTriviaApi(okHttpClient: OkHttpClient): TriviaApi {
        return Retrofit.Builder()
            .baseUrl(com.neo.trivia.data.api.ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TriviaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(api: TriviaApi): RemoteDataSource {
        return RemoteDataSourceImpl(api)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        database: TriviaDatabase,
        gson: Gson,
    ): LocalDataSource {
        return LocalDataSourceImpl(
            questionDao = database.questionDao(),
            favoriteDao = database.favoriteDao(),
            quizResultDao = database.quizResultDao(),
            categoryDao = database.categoryDao(),
            gson = gson,
        )
    }
}
