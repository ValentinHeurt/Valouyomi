package com.example.valouyomi.presentation.shared

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.valouyomi.domain.models.Chapter
import com.example.valouyomi.domain.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MangaChapterSharedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel(){
    val chapters: List<Chapter> = emptyList()
}