package com.example.a6_123140125.data.repository

import com.example.a6_123140125.data.api.NewsApiService
import com.example.a6_123140125.data.model.NewsPost

class NewsRepository(private val apiService: NewsApiService) {
    suspend fun getNews(): List<NewsPost> {
        return apiService.getPosts()
    }
}
