package com.example.valouyomi.presentation.manga_search

import com.example.valouyomi.domain.models.MangaThumbnail

data class GenreState(
    val isLoading: Boolean = false,
    val genres: MutableMap<String,Boolean> = mutableMapOf(),
    val error: String = ""
)