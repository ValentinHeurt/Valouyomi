package com.example.valouyomi.presentation.manga_search

import com.example.valouyomi.domain.models.MangaThumbnail

data class MangaSearchState(
    val isLoading: Boolean = false,
    val mangaThumbnails: List<MangaThumbnail> = emptyList(),
    val error: String = ""
)
