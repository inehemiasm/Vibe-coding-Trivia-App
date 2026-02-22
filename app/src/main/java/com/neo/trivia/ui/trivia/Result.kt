package com.neo.trivia.ui.trivia

import com.neo.trivia.domain.model.Category

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

fun <T> Result<T>.isSuccess() = this is Result.Success
fun <T> Result<T>.isError() = this is Result.Error