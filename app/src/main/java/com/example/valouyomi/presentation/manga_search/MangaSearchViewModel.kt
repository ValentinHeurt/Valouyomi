package com.example.valouyomi.presentation.manga_search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigator
import com.example.valouyomi.common.Constants
import com.example.valouyomi.common.Resource
import com.example.valouyomi.domain.models.MangaThumbnail
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
    var page = 1
    val mangaRepository = mangaRepositoryMap[param]?: throw java.lang.IllegalArgumentException("No dependency found for : $param")

    //Search state
    val mangaThumbnails: MutableLiveData<List<MangaThumbnail>> = MutableLiveData()
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val error: MutableState<String> = mutableStateOf("")

    private val _genresState = mutableStateOf(GenreState())
    val genresState: State<GenreState> = _genresState

    val isNewThumbnailsLoading: MutableState<Boolean> = mutableStateOf(false)

    val searchAppBarState: MutableState<SearchAppBarState> = mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")
    var trailingIconState: MutableState<TrailingIconState> = mutableStateOf(TrailingIconState.DELETE)
    init {
        getMangaThumbnails()
        getGenre()
    }

    fun getMangaThumbnails(){
        page = 1
        mangaRepository.searchManga(textSearch = if (searchTextState.value == "") null else searchTextState.value).onEach { result ->
            when(result){
                is Resource.Success -> {
                    mangaThumbnails.value = result.data ?: emptyList()
                }
                is Resource.Error -> {
                    error.value = result.message ?: "Unknown error"
                }
                is Resource.Loading -> {
                    isLoading.value = true
                }
            }

        }.launchIn(viewModelScope)
    }

    fun addMangaThumbnails(){
        page++
        mangaRepository.searchManga(
            textSearch = if (searchTextState.value == "") null else searchTextState.value,
            page = page.toString()
        ).onEach { result ->
            when(result){
                is Resource.Success -> {
                    var tempMangaList: MutableList<MangaThumbnail> = mangaThumbnails.value?.toMutableList()?: mutableListOf()
                    tempMangaList.addAll(result.data ?: ArrayList<MangaThumbnail>())
                    mangaThumbnails.value = tempMangaList ?: emptyList()
                    isNewThumbnailsLoading.value = false
                }
                is Resource.Error -> {
                    error.value = result.message ?: "Unknown error"
                    isNewThumbnailsLoading.value = false
                }
                is Resource.Loading -> {
                    isNewThumbnailsLoading.value = true
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