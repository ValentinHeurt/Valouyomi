package com.example.valouyomi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.valouyomi.common.BottomNavItem
import com.example.valouyomi.common.Constants
import com.example.valouyomi.presentation.Screen
import com.example.valouyomi.presentation.components.BottomNavigationBar
import com.example.valouyomi.presentation.components.Navigation
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
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                items = listOf(
                                    BottomNavItem(
                                        name = "Library",
                                        route = Screen.LibraryScreen.route,
                                        icon = Icons.Default.Home
                                    ),
                                    BottomNavItem(
                                        name = "Browse",
                                        route = Screen.ProviderListScreen.route,
                                        icon = Icons.Default.Search
                                    ),
                                    BottomNavItem(
                                        name = "Settings",
                                        route = Screen.SettingsScreen.route,
                                        icon = Icons.Default.Settings,
                                        badgeCount = 15
                                    )
                                ),
                                navController = navController,
                                onItemClicked = {
                                    navController.navigate(it.route)
                                })
                        }
                    ) { paddingValues ->
                        Navigation(navController = navController, modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()))
                    }
                }
            }
        }
    }
}