package com.example.tugaspam2.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.tugaspam2.model.News

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(viewModel: NewsViewModel) {

    val news by viewModel.filteredNews.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val readCount by viewModel.readCount.collectAsState()

    val categories = listOf("Health", "Education", "Travel", "Science")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TugasPAM2 - News Feed") },
                actions = {
                    Text(
                        text = "Read: $readCount",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { viewModel.changeCategory(category) },
                        label = { Text(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(news) { item ->
                    NewsItem(item, viewModel)
                }
            }
        }
    }
}

@Composable
fun NewsItem(news: News, viewModel: NewsViewModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.markAsRead()
                viewModel.fetchDetailAsync(news.content) {}
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = news.title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = news.category,
                color = when (news.category) {
                    "Health" -> Color.Red
                    "Education" -> Color.Blue
                    "Travel" -> Color.Magenta
                    else -> Color.Green
                }
            )
        }
    }
}
