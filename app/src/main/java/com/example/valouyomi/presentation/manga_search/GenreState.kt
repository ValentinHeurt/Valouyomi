package com.example.valouyomi.presentation.manga_search

import androidx.compose.runtime.MutableState
import com.example.valouyomi.common.CheckBoxState
import com.example.valouyomi.domain.models.MangaThumbnail

data class GenreState(
    val isLoading: Boolean = false,
    val genres: MutableMap<String,MutableState<CheckBoxState>> = mutableMapOf(),
    val error: String = ""
)