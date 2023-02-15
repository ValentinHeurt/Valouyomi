package com.example.valouyomi.presentation.manga_search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valouyomi.common.Resource
import com.example.valouyomi.domain.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MangaSearchViewModel @Inject constructor(
    private val mangaRepository: MangaRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _mangaThumbnailsState = mutableStateOf(MangaSearchState())
    val mangaThumbnailsState: State<MangaSearchState> = _mangaThumbnailsState

    private val _genresState = mutableStateOf(GenreState())
    val genresState: State<GenreState> = _genresState
    init {
        getMangaThumbnails()
        getGenre()
    }

    private fun getMangaThumbnails(){
        mangaRepository.searchManga().onEach { result ->
            when(result){
                is Resource.Success -> {
                    _mangaThumbnailsState.value = MangaSearchState(mangaThumbnails = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _mangaThumbnailsState.value = MangaSearchState(error = result.message ?: "Unknown error")
                }
                is Resource.Loading -> {
                    _mangaThumbnailsState.value = MangaSearchState(isLoading = true)
                }
            }

        }.launchIn(viewModelScope)
    }
    private fun getGenre(){
        mangaRepository.getGenres().onEach { result ->
            when(result){
                is Resource.Success -> {
                    _genresState.value = GenreState(genres = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _genresState.value = GenreState(error = result.message ?: "Unknown error")
                }
                is Resource.Loading -> {
                    _genresState.value = GenreState(isLoading = true)
                }
            }
        }
    }

}