package com.example.tugaspam3.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.tugaspam3.db.NoteDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NoteRepositoryTest {
    private lateinit var repository: NoteRepository
    private lateinit var database: NoteDatabase

    @Before
    fun setup() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        NoteDatabase.Schema.create(driver)
        database = NoteDatabase(driver)
        // Menguji implementasi konkret
        repository = NoteRepositoryImpl(database)
    }

    @Test
    fun `insertNote and getAllNotes returns correct list`() = runBlocking {
        // Arrange
        repository.insertNote("Title 1", "Content 1")
        
        // Act
        val notes = repository.getAllNotes().first()
        
        // Assert
        assertEquals(1, notes.size)
        assertEquals("Title 1", notes[0].title)
    }

    @Test
    fun `deleteNote removes note from database`() = runBlocking {
        repository.insertNote("To Delete", "Content", id = 1L)
        repository.deleteNote(1L)
        val notes = repository.getAllNotes().first()
        assertTrue(notes.isEmpty())
    }

    @Test
    fun `searchNotes returns filtered results`() = runBlocking {
        repository.insertNote("Apple", "Fruit")
        repository.insertNote("Banana", "Fruit")
        val results = repository.searchNotes("Apple").first()
        assertEquals(1, results.size)
        assertEquals("Apple", results[0].title)
    }

    @Test
    fun `toggleFavorite updates favorite status`() = runBlocking {
        repository.insertNote("Fav Note", "Content", id = 1L)
        repository.toggleFavorite(1L, true)
        val notes = repository.getAllNotes().first()
        // SQLDelight mapToList may return notes where isFavorite is true
        assertTrue(notes.any { it.isFavorite })
    }

    @Test
    fun `insertNote with existing id updates the note`() = runBlocking {
        repository.insertNote("Original", "Content", id = 1L)
        repository.insertNote("Updated", "Updated Content", id = 1L)
        val notes = repository.getAllNotes().first()
        assertEquals(1, notes.size)
        assertEquals("Updated", notes[0].title)
    }
}
