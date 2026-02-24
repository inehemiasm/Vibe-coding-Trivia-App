package com.neo.trivia.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * A generic wrapper class representing a Result with success, loading, or error state.
 * Used for handling API and database operations that can fail.
 *
 * @param T The type of data that can be wrapped in a Result
 */
sealed class Result<out T> {
    /**
     * Indicates successful completion of an operation.
     * @param T The type of the data returned by the operation
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Indicates an operation is in progress or has completed but is still loading.
     * @param isLoading Whether the operation is currently loading
     */
    data class Loading(val isLoading: Boolean = true) : Result<Nothing>()

    /**
     * Indicates an operation failed with an error.
     * @param error The exception that caused the failure
     */
    data class Failure(val error: Throwable) : Result<Nothing>()
}

/**
 * Extension function to convert a Flow operation into a Flow<Result>.
 * Wraps any exceptions in Result.Failure.
 * @param block The suspend operation to perform
 * @return Flow containing Result
 */
inline fun <T> Flow<Result<T>>.catchToFlow(): Flow<Result<T>> {
    return catch { emit(Result.Failure(it)) }
}

/**
 * Extension function to map a Flow of Results, transforming the data while preserving
 * the loading and error states.
 * @param transform The transformation function to apply to successful results
 * @return Flow containing transformed Result
 */
inline fun <T, R> Flow<Result<T>>.mapData(crossinline transform: (T) -> R): Flow<Result<R>> {
    return map { result ->
        when (result) {
            is Result.Success -> Result.Success(transform(result.data))
            is Result.Loading -> result
            is Result.Failure -> result
        }
    }
}