package com.example.tugaspam3.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.tugaspam3.db.NoteDatabase
import com.example.tugaspam3.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Interface untuk abstraksi (Rapi & Mudah di-test)
interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun searchNotes(query: String): Flow<List<Note>>
    suspend fun insertNote(title: String, content: String, id: Long? = null)
    suspend fun deleteNote(id: Long)
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean)
}

class NoteRepositoryImpl(database: NoteDatabase) : NoteRepository {
    private val queries = database.noteEntityQueries

    override fun getAllNotes(): Flow<List<Note>> {
        return queries.getAllNotes()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toNote() } }
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return queries.searchNotes(query, query)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toNote() } }
    }

    override suspend fun insertNote(title: String, content: String, id: Long?) {
        queries.insertNote(
            id = id,
            title = title,
            content = content,
            timestamp = System.currentTimeMillis(),
            isFavorite = false
        )
    }

    override suspend fun deleteNote(id: Long) {
        queries.deleteNote(id)
    }

    override suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        queries.updateFavorite(isFavorite, id)
    }

    private fun com.example.tugaspam3.db.NoteEntity.toNote(): Note {
        return Note(id, title, content, timestamp, isFavorite)
    }
}
