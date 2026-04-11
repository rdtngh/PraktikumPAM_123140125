package com.example.a6_123140125

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.a6_123140125.data.api.RetrofitInstance
import com.example.a6_123140125.data.repository.NewsRepository
import com.example.a6_123140125.ui.NewsDetailScreen
import com.example.a6_123140125.ui.NewsListScreen
import com.example.a6_123140125.ui.theme._6_123140125Theme
import com.example.a6_123140125.ui.viewmodel.NewsUiState
import com.example.a6_123140125.ui.viewmodel.NewsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _6_123140125Theme {
                NewsApp()
            }
        }
    }
}

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val repository = remember { NewsRepository(RetrofitInstance.api) }
    val viewModel: NewsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NewsViewModel(repository) as T
            }
        }
    )

    NavHost(navController = navController, startDestination = "news_list") {
        composable("news_list") {
            NewsListScreen(
                viewModel = viewModel,
                onArticleClick = { article ->
                    navController.navigate("news_detail/${article.id}")
                }
            )
        }
        composable(
            route = "news_detail/{articleId}",
            arguments = listOf(navArgument("articleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt("articleId")
            val uiState = viewModel.uiState.collectAsState().value
            
            if (uiState is NewsUiState.Success) {
                val article = uiState.news.find { it.id == articleId }
                article?.let {
                    NewsDetailScreen(article = it, onBack = { navController.popBackStack() })
                }
            }
        }
    }
}
