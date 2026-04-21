package com.example.tugaspam3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tugaspam3.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NotesState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false
)

class NotesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NotesState())
    val uiState: StateFlow<NotesState> = _uiState.asStateFlow()

    fun addNote(title: String, content: String) {
        val newNote = Note(
            id = (_uiState.value.notes.maxOfOrNull { it.id } ?: 0) + 1,
            title = title,
            content = content
        )
        _uiState.update { it.copy(notes = it.notes + newNote) }
    }

    fun updateNote(id: Int, title: String, content: String) {
        _uiState.update { state ->
            state.copy(
                notes = state.notes.map {
                    if (it.id == id) it.copy(title = title, content = content) else it
                }
            )
        }
    }

    fun deleteNote(id: Int) {
        _uiState.update { state ->
            state.copy(notes = state.notes.filter { it.id != id })
        }
    }

    fun toggleFavorite(id: Int) {
        _uiState.update { state ->
            state.copy(
                notes = state.notes.map {
                    if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
                }
            )
        }
    }

    fun getNoteById(id: Int): Note? {
        return _uiState.value.notes.find { it.id == id }
    }
}
