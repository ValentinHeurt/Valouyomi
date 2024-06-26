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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.valouyomi.presentation.components.BottomNavItem
import com.example.valouyomi.presentation.Screen
import com.example.valouyomi.presentation.components.BottomNavigationBar
import com.example.valouyomi.presentation.components.Navigation
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
                    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                    val visiblesRoutes = listOf(
                        Screen.SettingsScreen.route,
                        Screen.LibraryScreen.route,
                        Screen.ProviderListScreen.route
                    )
                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    Scaffold(
                        bottomBar = {
                            if (visiblesRoutes.contains(navBackStackEntry?.destination?.route)){
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
                        }
                    ) { paddingValues ->
                        Navigation(navController = navController, modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()))
                    }
                }
            }
        }
    }
}