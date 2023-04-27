package com.example.valouyomi.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.valouyomi.common.Constants
import com.example.valouyomi.presentation.Screen
import com.example.valouyomi.presentation.library.LibraryScreen
import com.example.valouyomi.presentation.manga_details.MangaScreen
import com.example.valouyomi.presentation.manga_reader.MangaReaderScreen
import com.example.valouyomi.presentation.manga_search.MangaSearchScreen
import com.example.valouyomi.presentation.providers.ProviderListScreen
import com.example.valouyomi.presentation.settings.SettingsScreen

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier){
    NavHost(navController = navController, startDestination = Screen.LibraryScreen.route, modifier = modifier ){
        composable(Screen.LibraryScreen.route){
            LibraryScreen(navController = navController)
        }
        composable(Screen.MangaSearchScreen.route + "/{${Constants.PROVIDER_PARAM}}"){
            MangaSearchScreen(navController = navController)
        }
        composable(Screen.SettingsScreen.route){
            SettingsScreen(navController = navController)
        }
        composable(Screen.ProviderListScreen.route){
            ProviderListScreen(navController = navController)
        }
        composable(Screen.MangaScreen.route + "/{${Constants.MANGA_URL_PARAM}}/{${Constants.PROVIDER_PARAM}}"){
            MangaScreen(navController = navController)
        }
        composable(Screen.MangaReaderScreen.route + "/{${Constants.CHAPTER_URL_PARAM}}/{${Constants.PROVIDER_PARAM}}"){
            MangaReaderScreen(navController = navController)
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClicked: (BottomNavItem) -> Unit
){
    val mainRoutes = listOf(Screen.LibraryScreen.route, Screen.SettingsScreen.route, Screen.ProviderListScreen.route)
    val backStackEntry = navController.currentBackStackEntryAsState()
    var lastSelected : String = "None"
    if(backStackEntry.value?.destination?.route != Screen.MangaSearchScreen.route) {
        BottomNavigation(
            modifier = modifier,
            backgroundColor = Color.Gray,
            elevation = 5.dp
        ) {
            items.forEach { item ->
                val selected =
                    (item.route == backStackEntry.value?.destination?.route) || (!mainRoutes.contains(
                        backStackEntry.value?.destination?.route
                    ) && lastSelected == item.route)
                if (selected) lastSelected = item.route
                BottomNavigationItem(
                    selected = selected,
                    onClick = { onItemClicked(item) },
                    selectedContentColor = Color.Blue,
                    unselectedContentColor = Color.DarkGray,
                    icon = {
                        Column(horizontalAlignment = CenterHorizontally) {
                            if (item.badgeCount > 0) {
                                BadgedBox(
                                    badge = { Badge { Text(text = item.badgeCount.toString()) } }
                                ) {
                                    Icon(imageVector = item.icon, contentDescription = item.name)
                                }
                            } else {
                                Icon(imageVector = item.icon, contentDescription = item.name)
                            }
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                )
            }

        }
    }
}