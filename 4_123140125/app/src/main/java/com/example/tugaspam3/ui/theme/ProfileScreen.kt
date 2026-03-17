package com.example.tugaspam3.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tugaspam3.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileHeader()

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode")
            Switch(
                checked = state.isDarkMode,
                onCheckedChange = { viewModel.toggleDarkMode() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProfileCard(state)

        Spacer(modifier = Modifier.height(16.dp))

        EditProfileForm(
            state = state,
            onSave = { name, bio ->
                viewModel.updateProfile(name, bio)
            }
        )
    }
}
