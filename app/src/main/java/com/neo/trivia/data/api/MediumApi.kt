package com.neo.trivia.data.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface MediumApi {
    @GET
    suspend fun fetchRssFeed(@Url url: String): ResponseBody
}
