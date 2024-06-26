package com.example.valouyomi.presentation.manga_search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigator
import com.example.valouyomi.common.CheckBoxState
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

    val sortList: MutableState<Map<String, String>> = mutableStateOf(emptyMap())

    private val _genresState = mutableStateOf(GenreState())
    val genresState: State<GenreState> = _genresState

    val isNewThumbnailsLoading: MutableState<Boolean> = mutableStateOf(false)

    val searchAppBarState: MutableState<SearchAppBarState> = mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")
    var trailingIconState: MutableState<TrailingIconState> = mutableStateOf(TrailingIconState.DELETE)

    val selectedSortProtocol: MutableState<String> = mutableStateOf("")


    init {
        getMangaThumbnails()
        getGenre()
        sortList.value = mangaRepository.getSortMap()
        //selectedSortProtocol.value = sortList.value.keys.first()
    }

    fun getMangaThumbnails(){
        page = 1
        mangaRepository.searchManga(
            textSearch = if (searchTextState.value == "") null else searchTextState.value,
            includedGenres = genresState.value.genres.filter { it.value.value == CheckBoxState.SELECTED }.keys.toList(),
            excludedGenres = genresState.value.genres.filter { it.value.value == CheckBoxState.EXCLUDED }.keys.toList(),
            orderBy = sortList.value[selectedSortProtocol.value]
        ).onEach { result ->
            when(result){
                is Resource.Success -> {
                    mangaThumbnails.value = result.data ?: emptyList()
                    isLoading.value = false
                }
                is Resource.Error -> {
                    error.value = result.message ?: "Unknown error"
                    mangaThumbnails.value = emptyList()
                    isLoading.value = false
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
            includedGenres = genresState.value.genres.filter { it.value.value == CheckBoxState.SELECTED }.keys.toList(),
            excludedGenres = genresState.value.genres.filter { it.value.value == CheckBoxState.EXCLUDED }.keys.toList(),
            orderBy = sortList.value[selectedSortProtocol.value],
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
                    println("s")
                    _genresState.value = GenreState(genres = result.data?.map{ it to mutableStateOf(CheckBoxState.UNSELECTED) }?.toMap()?.toMutableMap() ?: mutableMapOf())
                }
                is Resource.Error -> {
                    println("e")
                    _genresState.value = GenreState(error = result.message ?: "Unknown error")
                }
                is Resource.Loading -> {
                    println("l")
                    _genresState.value = GenreState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}