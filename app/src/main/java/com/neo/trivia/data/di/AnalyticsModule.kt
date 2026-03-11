package com.neo.trivia.data.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.neo.trivia.data.remote.FirebaseAnalyticsHelper
import com.neo.trivia.domain.analytics.AnalyticsHelper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsHelper(
        firebaseAnalyticsHelper: FirebaseAnalyticsHelper
    ): AnalyticsHelper

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
            return FirebaseAnalytics.getInstance(context)
        }
    }
}
