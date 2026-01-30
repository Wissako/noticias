package com.example.noticias.network

import com.example.noticias.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("sources") sources: String? = null,
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("q") query: String? = null
    ): NewsResponse
}