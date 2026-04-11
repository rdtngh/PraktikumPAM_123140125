package com.example.a6_123140125.data.api

import com.example.a6_123140125.data.model.NewsPost
import retrofit2.http.GET

interface NewsApiService {
    @GET("posts")
    suspend fun getPosts(): List<NewsPost>
}
