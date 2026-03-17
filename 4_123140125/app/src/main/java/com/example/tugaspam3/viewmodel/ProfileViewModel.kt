package com.example.tugaspam3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tugaspam3.model.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(
            name = "Raditya Alrasyid Nugroho",
            nim = "123140125",
            bio = "Mahasiswa Teknik Informatika ITERA",
            email = "raditya@student.itera.ac.id",
            phone = "+628123456789",
            location = "Lampung"
        )
    )

    val uiState: StateFlow<ProfileUiState> = _uiState

    fun updateProfile(name: String, bio: String) {
        _uiState.update {
            it.copy(name = name, bio = bio)
        }
    }

    fun toggleDarkMode() {
        _uiState.update {
            it.copy(isDarkMode = !it.isDarkMode)
        }
    }
}