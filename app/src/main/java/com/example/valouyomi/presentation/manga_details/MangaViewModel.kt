package com.example.valouyomi.presentation.manga_details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valouyomi.common.Constants
import com.example.valouyomi.common.Resource
import com.example.valouyomi.domain.models.Manga
import com.example.valouyomi.domain.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mangaRepositoryMap: Map<String, @JvmSuppressWildcards MangaRepository>
): ViewModel(){

    val urlParam = URLDecoder.decode(savedStateHandle.get<String>(Constants.MANGA_URL_PARAM), StandardCharsets.UTF_8.toString())
    val providerParam = savedStateHandle.get<String>(Constants.PROVIDER_PARAM).toString()
    val manga: MutableState<Manga> = mutableStateOf(Manga())
    val mangaRepository = mangaRepositoryMap[providerParam]?: throw java.lang.IllegalArgumentException("No dependency found for : $providerParam")

    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val error: MutableState<String> = mutableStateOf("")

    init {
        getManga(urlParam)
    }

    fun getManga(url: String){
        mangaRepository.getManga(url).onEach { result ->
            when(result){
                is Resource.Success -> {
                    manga.value = result.data ?: Manga()
                    manga.value.chapters = manga.value.chapters.reversed()
                }
                is Resource.Error -> {
                    error.value = result.message ?: "Unknown error"
                    isLoading.value = false
                }
                is Resource.Loading -> {
                    isLoading.value = true
                }
            }
        }.launchIn(viewModelScope)
    }

}