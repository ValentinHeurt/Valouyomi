package com.example.valouyomi.presentation.manga_search

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.valouyomi.domain.models.MangaThumbnail
import com.example.valouyomi.domain.repository.MangaRepository
import com.example.valouyomi.domain.use_case.get_genres.GetGenresUseCase
import com.example.valouyomi.domain.use_case.search_manga.SearchMangaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MangaSearchViewModel constructor(
    private val mangaRepository: MangaRepository
): ViewModel(){

    var mangaThumbnails = mutableStateListOf<MangaThumbnail>()
        private set

    init {
    }

}