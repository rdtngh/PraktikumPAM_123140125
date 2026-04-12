package com.example.a6_123140125.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.a6_123140125.data.model.NewsPost
import com.example.a6_123140125.ui.viewmodel.NewsUiState
import com.example.a6_123140125.ui.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    viewModel: NewsViewModel,
    onArticleClick: (NewsPost) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(500.dp)
                .align(Alignment.TopStart)
                .offset(x = (-150).dp, y = (-100).dp)
                .background(Color(0xFF3B82F6).copy(alpha = 0.3f), CircleShape)
                .blur(100.dp)
        )
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 150.dp)
                .background(Color(0xFFD946EF).copy(alpha = 0.25f), CircleShape)
                .blur(100.dp)
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Pusat Berita",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp
                            ),
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White.copy(alpha = 0.05f)
                    ),
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(32.dp))
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.fetchNews() },
                    containerColor = Color.White.copy(alpha = 0.2f),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                        .shadow(30.dp, RoundedCornerShape(24.dp)),
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Perbarui")
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                when (val state = uiState) {
                    is NewsUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White
                        )
                    }
                    is NewsUiState.Success -> {
                        NewsList(
                            news = state.news, 
                            isPaginatedLoading = state.isPaginatedLoading,
                            onArticleClick = onArticleClick,
                            onLoadMore = { viewModel.loadMore() }
                        )
                    }
                    is NewsUiState.Error -> {
                        ErrorState(message = state.message, onRetry = { viewModel.fetchNews() })
                    }
                }
            }
        }
    }
}

@Composable
fun NewsList(
    news: List<NewsPost>, 
    isPaginatedLoading: Boolean,
    onArticleClick: (NewsPost) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            items = news,
            key = { _, article -> article.id }
        ) { index, article ->
            if (index == news.size - 1 && !isPaginatedLoading) {
                LaunchedEffect(Unit) {
                    onLoadMore()
                }
            }
            
            NewsItem(article = article, onClick = { onArticleClick(article) })
        }
        
        if (isPaginatedLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(36.dp),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Memuat berita lainnya...",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(article: NewsPost, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.12f),
                        Color.White.copy(alpha = 0.04f)
                    )
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(28.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            ) {
                AsyncImage(
                    model = article.getSafeImageUrl(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.width(18.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 22.sp,
                        letterSpacing = (-0.4).sp
                    ),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = article.body,
                    style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = 18.sp
                    ),
                    color = Color.White.copy(alpha = 0.65f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(article: NewsPost, onBack: () -> Unit) {
    val scrollState = rememberScrollState()
    
    // Hitung alpha berdasarkan scroll (akan menghilang perlahan setelah scroll 100px)
    val titleAlpha by remember {
        derivedStateOf {
            val threshold = 100f
            if (scrollState.value > threshold) {
                (1f - (scrollState.value - threshold) / 100f).coerceIn(0f, 1f)
            } else {
                1f
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-150).dp)
                .background(Color(0xFF3B82F6).copy(alpha = 0.15f), CircleShape)
                .blur(100.dp)
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            "Detail Berita", 
                            color = Color.White.copy(alpha = titleAlpha), 
                            fontWeight = FontWeight.Bold 
                        ) 
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .padding(12.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                                .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                AsyncImage(
                    model = article.getSafeImageUrl(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .padding(20.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(32.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(32.dp))
                        .padding(28.dp)
                ) {
                    Column {
                        Surface(
                            color = Color(0xFF3B82F6).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.border(1.dp, Color(0xFF3B82F6).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        ) {
                            Text(
                                text = "BERITA TERBARU",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF60A5FA)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(18.dp))
                        
                        Text(
                            text = article.title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp,
                                lineHeight = 34.sp
                            ),
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.15f))
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Text(
                            text = article.body,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                lineHeight = 30.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        )
                        
                        repeat(2) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = article.body,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    lineHeight = 30.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gagal memuat",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(36.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Coba Lagi", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
