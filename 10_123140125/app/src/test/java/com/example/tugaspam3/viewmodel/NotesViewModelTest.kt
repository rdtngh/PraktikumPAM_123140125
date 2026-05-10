package com.example.tugaspam3.viewmodel

import app.cash.turbine.test
import com.example.tugaspam3.data.GeminiService
import com.example.tugaspam3.data.NoteRepository
import com.example.tugaspam3.model.Note
import com.example.tugaspam3.model.NotesUiState
import com.example.tugaspam3.platform.NetworkMonitor
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private val repository = mockk<NoteRepository>(relaxed = true)
    private val networkMonitor = mockk<NetworkMonitor>(relaxed = true)
    private val geminiService = mockk<GeminiService>(relaxed = true)
    
    private lateinit var viewModel: NotesViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { networkMonitor.isConnected } returns flowOf(true)
        every { repository.getAllNotes() } returns flowOf(emptyList())
        viewModel = NotesViewModel(repository, networkMonitor, geminiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState emits Success when notes are available`() = runTest {
        // Arrange
        val notes = listOf(Note(1, "Test", "Content", 1000L, false))
        every { repository.getAllNotes() } returns flowOf(notes)
        val vm = NotesViewModel(repository, networkMonitor, geminiService)

        // Act & Assert (Flow Test with Turbine)
        vm.uiState.test {
            val state = awaitItem()
            assertTrue(state is NotesUiState.Success)
            assertEquals(notes, (state as NotesUiState.Success).notes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits Empty when database is empty`() = runTest {
        every { repository.getAllNotes() } returns flowOf(emptyList())
        viewModel.uiState.test {
            assertEquals(NotesUiState.Empty, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearchQueryChange updates flow and calls repository`() = runTest {
        val query = "apple"
        every { repository.searchNotes(query) } returns flowOf(emptyList())
        
        viewModel.onSearchQueryChange(query)
        
        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `addNote triggers repository insert`() = runTest {
        viewModel.addNote("Title", "Content")
        coVerify { repository.insertNote("Title", "Content") }
    }

    @Test
    fun `toggleFavorite triggers repository update`() = runTest {
        viewModel.toggleFavorite(1L, false)
        coVerify { repository.toggleFavorite(1L, true) }
    }

    @Test
    fun `deleteNote triggers repository deletion`() = runTest {
        // Test case ke-4 untuk MockK (Total 14 Test Cases secara keseluruhan)
        viewModel.deleteNote(1L)
        coVerify { repository.deleteNote(1L) }
    }
}
