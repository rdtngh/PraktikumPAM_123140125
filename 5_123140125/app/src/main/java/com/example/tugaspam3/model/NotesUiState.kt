package com.example.tugaspam3.model

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false
)
