package com.neo.trivia.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApi {
    @GET(ApiConstants.API_VERSION)
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int? = null,
        @Query("type") type: String = "multiple"
    ): Response<QuestionResponse>
}