package com.example.valouyomi.presentation

sealed class Screen(val route: String){
    object MangaSearchScreen: Screen("manga_search_screen")
    object ProviderListScreen: Screen("provider_list_screen")
    object SettingsScreen: Screen("parameters_screen")
    object LibraryScreen: Screen("library_screen")
}
