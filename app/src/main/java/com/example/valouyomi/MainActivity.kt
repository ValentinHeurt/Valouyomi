package com.example.valouyomi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.valouyomi.presentation.Screen
import com.example.valouyomi.presentation.manga_search.MangaSearchScreen
import com.example.valouyomi.presentation.providers.ProviderListScreen
import com.example.valouyomi.ui.theme.ValouyomiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ValouyomiTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.ProviderListScreen.route
                    ) {
                        composable(
                            route = Screen.ProviderListScreen.route
                        ){
                            ProviderListScreen(navController = navController)
                        }
                        composable(
                            route = Screen.MangaSearchScreen.route
                        ){
                            MangaSearchScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}