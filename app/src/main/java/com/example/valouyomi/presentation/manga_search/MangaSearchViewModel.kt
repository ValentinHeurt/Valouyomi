package com.example.valouyomi.presentation.manga_search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigator
import com.example.valouyomi.common.Constants
import com.example.valouyomi.common.Resource
import com.example.valouyomi.domain.repository.MangaRepository
import com.example.valouyomi.presentation.Screen
import com.example.valouyomi.presentation.manga_search.util.SearchAppBarState
import com.example.valouyomi.presentation.manga_search.util.TrailingIconState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@HiltViewModel
class MangaSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mangaRepositoryMap: Map<String, @JvmSuppressWildcards MangaRepository>
): ViewModel(){

    val param = savedStateHandle.get<String>(Constants.PROVIDER_PARAM).toString()

    val mangaRepository = mangaRepositoryMap[param]?: throw java.lang.IllegalArgumentException("No dependency found for : $param")
    private val _mangaThumbnailsState = mutableStateOf(MangaSearchState())
    val mangaThumbnailsState: State<MangaSearchState> = _mangaThumbnailsState
    private val _genresState = mutableStateOf(GenreState())
    val genresState: State<GenreState> = _genresState

    val searchAppBarState: MutableState<SearchAppBarState> = mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")
    var trailingIconState: MutableState<TrailingIconState> = mutableStateOf(TrailingIconState.DELETE)
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