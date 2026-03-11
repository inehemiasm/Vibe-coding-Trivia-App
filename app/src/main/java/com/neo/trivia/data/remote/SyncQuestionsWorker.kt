package com.neo.trivia.data.remote

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.neo.trivia.data.preferences.SyncPreferencesManager
import com.neo.trivia.domain.usecase.SyncQuestionsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class SyncQuestionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncQuestionsUseCase: SyncQuestionsUseCase,
    private val syncPreferencesManager: SyncPreferencesManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Timber.d("SyncQuestionsWorker: Starting background sync")
        return try {
            syncQuestionsUseCase(targetAmountPerCategory = 20)
            syncPreferencesManager.updateLastSyncTime()
            Timber.d("SyncQuestionsWorker: Background sync successful")
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "SyncQuestionsWorker: Background sync failed")
            Result.retry()
        }
    }
}
