package com.example.valouyomi.presentation.manga_search

import com.example.valouyomi.domain.models.MangaThumbnail

data class GenreState(
    val isLoading: Boolean = false,
    val genres: List<String> = emptyList(),
    val error: String = ""
)