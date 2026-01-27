package com.example.noticias
import com.example.noticias.BuildConfig
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noticias.ui.NewsScreen
import com.example.noticias.ui.NewsViewModel
import com.example.noticias.ui.theme.NoticiasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val apiKey = BuildConfig.NEWS_API_KEY
        setContent {
            NoticiasTheme {
                val viewModel: NewsViewModel = viewModel()
                NewsScreen(viewModel = viewModel, apiKey = apiKey)
            }
        }
    }
}