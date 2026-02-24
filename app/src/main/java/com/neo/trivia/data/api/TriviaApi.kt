package com.neo.trivia.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String,
        @Query("type") type: String = "multiple"
    ): Response<QuestionResponse>

    @GET("api_category.php")
    suspend fun getCategories(): Response<CategoryResponse>
}

data class CategoryResponse(
    @SerializedName("trivia_categories")
    val triviaCategories: List<ApiCategory>
)

data class ApiCategory(
    val id: Int,
    val name: String
)