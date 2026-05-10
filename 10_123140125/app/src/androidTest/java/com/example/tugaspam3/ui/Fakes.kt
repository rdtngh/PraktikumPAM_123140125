package com.example.tugaspam3.ui

import com.example.tugaspam3.data.GeminiService
import com.example.tugaspam3.data.NoteRepository
import com.example.tugaspam3.model.Note
import com.example.tugaspam3.platform.NetworkMonitor
import com.google.ai.client.generativeai.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

// Fake Implementation: Stabil, Cepat, dan Anti-Error libmockkjvmtiagent
class FakeNoteRepository : NoteRepository {
    val notesFlow = MutableStateFlow<List<Note>>(emptyList())
    
    override fun getAllNotes(): Flow<List<Note>> = notesFlow
    override fun searchNotes(query: String): Flow<List<Note>> = notesFlow
    override suspend fun insertNote(title: String, content: String, id: Long?) {}
    override suspend fun deleteNote(id: Long) {}
    override suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {}
}

class FakeNetworkMonitor : NetworkMonitor {
    override val isConnected: Flow<Boolean> = flowOf(true)
}

class FakeGeminiService : GeminiService {
    override fun summarizeNoteStream(content: String): Flow<String> = flowOf("")
    override fun startNewSession(noteContent: String): Chat = throw NotImplementedError()
    override fun sendMessageStream(message: String): Flow<String> = flowOf("")
}
