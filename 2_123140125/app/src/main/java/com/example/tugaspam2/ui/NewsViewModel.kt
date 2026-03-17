package com.example.tugaspam2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugaspam2.data.NewsRepository
import com.example.tugaspam2.model.News
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val repository = NewsRepository()

    private val _selectedCategory = MutableStateFlow("Health")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount

    private val _newsList = MutableStateFlow<List<News>>(emptyList())

    val filteredNews: StateFlow<List<News>> =
        combine(_newsList, _selectedCategory) { list, category ->
            list
                .filter { it.category == category }
                .map { it.copy(title = "📰 ${it.title}") }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        viewModelScope.launch {
            repository.getNewsStream().collect { news ->
                _newsList.value = _newsList.value + news
            }
        }
    }

    fun changeCategory(category: String) {
        _selectedCategory.value = category
    }

    fun markAsRead() {
        _readCount.value++
    }

    fun fetchDetailAsync(content: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            delay(1000)
            onResult("Detail async: $content")
        }
    }
}
