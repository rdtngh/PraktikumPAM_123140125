package com.example.tugaspam3.model

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false
)
