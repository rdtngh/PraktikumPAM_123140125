package com.example.tugaspam3.data

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GeminiService {
    fun summarizeNoteStream(content: String): Flow<String>
    fun startNewSession(noteContent: String): Chat
    fun sendMessageStream(message: String): Flow<String>
}

class GeminiServiceImpl(private val generativeModel: GenerativeModel) : GeminiService {

    private var chatSession: Chat? = null

    private val systemPrompt = """
        Anda adalah "Asisten Catatan AI" yang profesional dan ramah.
        Tugas Anda adalah membantu pengguna mengelola catatan dalam Bahasa Indonesia.
    """.trimIndent()

    override fun summarizeNoteStream(content: String): Flow<String> {
        val prompt = "$systemPrompt\n\nTolong buatkan ringkasan:\n\n$content"
        return generativeModel.generateContentStream(prompt).map { it.text ?: "" }
    }

    override fun startNewSession(noteContent: String): Chat {
        val history = listOf(
            content(role = "user") { text("Ini catatan saya: $noteContent") },
            content(role = "model") { text("Siap! Ada yang bisa saya bantu?") }
        )
        chatSession = generativeModel.startChat(history)
        return chatSession!!
    }

    override fun sendMessageStream(message: String): Flow<String> {
        val session = chatSession ?: throw IllegalStateException("Chat session belum dimulai")
        return session.sendMessageStream(message).map { it.text ?: "" }
    }
}
