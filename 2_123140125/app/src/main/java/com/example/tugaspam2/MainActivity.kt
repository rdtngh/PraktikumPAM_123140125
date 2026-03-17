package com.example.tugaspam2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tugaspam2.ui.NewsScreen
import com.example.tugaspam2.ui.NewsViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: NewsViewModel = viewModel()
            NewsScreen(viewModel)
        }
    }
}
