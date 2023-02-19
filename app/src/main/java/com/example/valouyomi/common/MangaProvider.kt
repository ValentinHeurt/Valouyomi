package com.example.valouyomi.common

import com.example.valouyomi.R
import com.example.valouyomi.presentation.Screen


enum class MangaProvider(
    val providerName: String,
    val imageID: Int,
    val lang: String,
    val route : String
) {
    MANGANATO("Manganato", R.drawable.manganato, "English",Screen.MangaSearchScreen.route)
}