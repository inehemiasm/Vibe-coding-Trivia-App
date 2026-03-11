package com.neo.trivia.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApi {
    @GET("v1/search")
    suspend fun searchPhotos(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 1
    ): Response<PexelsResponse>
}

data class PexelsResponse(
    val photos: List<PexelsPhoto>
)

data class PexelsPhoto(
    val src: PexelsPhotoSource
)

data class PexelsPhotoSource(
    val large: String,
    val medium: String
)
