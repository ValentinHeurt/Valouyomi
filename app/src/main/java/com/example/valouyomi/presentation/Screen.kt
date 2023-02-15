package com.example.valouyomi.presentation

sealed class Screen(val route: String){
    object MangaSearchScreen: Screen("manga_search_screen")
}
