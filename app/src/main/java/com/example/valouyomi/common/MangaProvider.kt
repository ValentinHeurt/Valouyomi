package com.example.valouyomi.common

import com.example.valouyomi.R
import com.example.valouyomi.domain.repository.MangaRepository
import com.example.valouyomi.presentation.Screen


enum class MangaProvider(
    val providerName: String,
    val imageID: Int,
    val lang: String,
    val route : String
) {
    MANGANATO(Constants.MANGANATO, R.drawable.manganato, "English",Screen.MangaSearchScreen.route)
}