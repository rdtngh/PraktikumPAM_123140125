package com.example.a6_123140125.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a6_123140125.data.model.NewsPost
import com.example.a6_123140125.data.repository.NewsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val news: List<NewsPost>, val isPaginatedLoading: Boolean = false) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState
    
    // Counter to ensure unique IDs for fake pagination
    private val idCounter = AtomicInteger(1000)

    init {
        fetchNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            try {
                // Fetch all and take first 6 to ensure it's enough to start but still small
                val allNews = repository.getNews().shuffled()
                val initialNews = allNews.take(6).map { 
                    it.copy(id = idCounter.getAndIncrement()) 
                }
                _uiState.value = NewsUiState.Success(initialNews)
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (currentState is NewsUiState.Success && !currentState.isPaginatedLoading) {
            viewModelScope.launch {
                _uiState.value = currentState.copy(isPaginatedLoading = true)
                
                try {
                    // Simulation delay
                    delay(1500) 
                    
                    // Get "new" data
                    val allNews = repository.getNews().shuffled()
                    val extraNews = allNews.take(5).map { 
                        it.copy(
                            id = idCounter.getAndIncrement(),
                            title = "[New] " + it.title // Visually mark as new
                        ) 
                    }

                    val updatedList = currentState.news + extraNews
                    _uiState.value = NewsUiState.Success(updatedList, isPaginatedLoading = false)
                } catch (e: Exception) {
                    _uiState.value = currentState.copy(isPaginatedLoading = false)
                }
            }
        }
    }
}
