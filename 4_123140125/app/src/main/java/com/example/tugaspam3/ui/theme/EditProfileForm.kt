package com.example.tugaspam3.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tugaspam3.model.ProfileUiState

@Composable
fun EditProfileForm(
    state: ProfileUiState,
    onSave: (String, String) -> Unit
) {

    var name by remember { mutableStateOf(state.name) }
    var bio by remember { mutableStateOf(state.bio) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text("Edit Profile")

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                onSave(name, bio)
            }) {
                Text("Save")
            }
        }
    }
}