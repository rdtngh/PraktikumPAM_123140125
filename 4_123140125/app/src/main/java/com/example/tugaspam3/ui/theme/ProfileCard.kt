package com.example.tugaspam3.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tugaspam3.model.ProfileUiState

@Composable
fun ProfileCard(state: ProfileUiState) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(state.name, fontWeight = FontWeight.Bold)
            Text(state.nim)
            Text(state.bio)

            Spacer(modifier = Modifier.height(8.dp))

            InfoItem(Icons.Default.Email, "Email", state.email)
            InfoItem(Icons.Default.Phone, "Phone", state.phone)
            InfoItem(Icons.Default.LocationOn, "Location", state.location)
        }
    }
}