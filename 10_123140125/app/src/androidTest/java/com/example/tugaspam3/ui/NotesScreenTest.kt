package com.example.tugaspam3.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.tugaspam3.model.Note
import com.example.tugaspam3.viewmodel.NotesViewModel
import org.junit.Rule
import org.junit.Test

class NotesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Menggunakan Fake Objects: Jauh lebih stabil dan anti-error arsitektur/native
    private val fakeRepository = FakeNoteRepository()
    private val fakeNetworkMonitor = FakeNetworkMonitor()
    private val fakeGeminiService = FakeGeminiService()

    @Test
    fun title_is_displayed() {
        // Arrange - AAA Pattern
        val viewModel = NotesViewModel(fakeRepository, fakeNetworkMonitor, fakeGeminiService)

        // Act
        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel, onNoteClick = {})
        }

        // Assert
        composeTestRule.onNodeWithText("Notes App").assertIsDisplayed()
    }

    @Test
    fun notes_list_is_displayed_on_success() {
        // Arrange
        val notes = listOf(
            Note(1, "Beli Susu", "Jangan lupa yang low fat", 1000L, false),
            Note(2, "Belajar Koin", "Implementasi DI di Android", 2000L, true)
        )
        fakeRepository.notesFlow.value = notes
        val viewModel = NotesViewModel(fakeRepository, fakeNetworkMonitor, fakeGeminiService)

        // Act
        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel, onNoteClick = {})
        }

        // Assert
        composeTestRule.onNodeWithText("Beli Susu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Belajar Koin").assertIsDisplayed()
    }

    @Test
    fun empty_message_is_displayed_when_no_notes() {
        // Arrange
        fakeRepository.notesFlow.value = emptyList()
        val viewModel = NotesViewModel(fakeRepository, fakeNetworkMonitor, fakeGeminiService)

        // Act
        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel, onNoteClick = {})
        }

        // Assert
        composeTestRule.onNodeWithText("Your story begins here...").assertIsDisplayed()
    }
}
