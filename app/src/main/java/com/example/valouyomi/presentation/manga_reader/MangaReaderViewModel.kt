package com.example.valouyomi.presentation.manga_reader

import android.graphics.pdf.PdfDocument.Page
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.valouyomi.common.Constants
import com.example.valouyomi.common.Resource
import com.example.valouyomi.domain.models.Chapter
import com.example.valouyomi.domain.repository.MangaRepository
import com.example.valouyomi.presentation.manga_reader.components.MangaPageImage
import com.example.valouyomi.presentation.shared.MangaChapterSharedViewModel
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.Headers
import okhttp3.internal.wait
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class MangaReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mangaRepositoryMap: Map<String, @JvmSuppressWildcards MangaRepository>
): ViewModel(){

    val urlParam = URLDecoder.decode(savedStateHandle.get<String>(Constants.CHAPTER_URL_PARAM), StandardCharsets.UTF_8.toString())
    val providerParam = savedStateHandle.get<String>(Constants.PROVIDER_PARAM).toString()
    var chaptersParam: List<Chapter>? = emptyList()
    val mangaNameParam = savedStateHandle.get<String>(Constants.MANGA_NAME_PARAM).toString()
    val currentChapterIndex = savedStateHandle.get<String>(Constants.CURRENT_CHAPTER_PARAM)?.toInt()
    val headers: MutableState<Headers> = mutableStateOf(Headers.headersOf("accept",""))

    val pages: MutableState<List<String>> = mutableStateOf(emptyList())
    val mangaRepository = mangaRepositoryMap[providerParam]?: throw java.lang.IllegalArgumentException("No dependency found for : $providerParam")
    var pageImageDatas: MutableState<List<MutableState<PageImageData>>> = mutableStateOf(emptyList())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val error: MutableState<String> = mutableStateOf("")

    val isScrollEnabled: MutableState<Boolean> = mutableStateOf(true)

    var composablesImages: List<@Composable () -> Unit> = emptyList()

    var boxHeight: MutableState<Int> = mutableStateOf(0)
    var boxWidth: MutableState<Int> = mutableStateOf(0)

    var pageList: MutableList<PageImageData> = arrayListOf()
    var pageUnitList: MutableList<@Composable () -> Unit> = arrayListOf()

    init {
        pageList = arrayListOf()
        getChapter(urlParam)
        headers.value = mangaRepository.getHeaders()
    }

    fun getChapter(url: String){
        mangaRepository.getPages(url).onEach { result ->
            when(result){
                is Resource.Success -> {
                    pages.value = result.data ?: emptyList()
                    setupPageList()
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

    fun setupPageList(){
        pageList = arrayListOf()
        pages.value.forEach(){ pageList.add(PageImageData(mutableStateOf(0f), mutableStateOf(0f), mutableStateOf(1f), mutableStateOf(Size.Zero))) }
        pages.value.forEachIndexed{ index, item -> pageUnitList.add(@Composable { MangaPageImage(url = item, pageImageData = pageList[index]) }) }
    }
}

data class PageImageData(var offsetX: MutableState<Float>, var offsetY: MutableState<Float>, var scaleFactor: MutableState<Float>, var imageSize: MutableState<Size>)