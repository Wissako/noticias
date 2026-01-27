package com.example.noticias.network

import com.example.noticias.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query
interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("sources") sources: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}