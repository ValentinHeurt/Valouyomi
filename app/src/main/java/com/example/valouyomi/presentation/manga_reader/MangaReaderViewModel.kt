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
import com.example.valouyomi.presentation.manga_reader.components.MangaFirstPage
import com.example.valouyomi.presentation.manga_reader.components.MangaLastPage
import com.example.valouyomi.presentation.manga_reader.components.MangaNextChapterPage
import com.example.valouyomi.presentation.manga_reader.components.MangaPageImage
import com.example.valouyomi.presentation.manga_reader.components.MangaPreviousChapterPage
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
    var chaptersParam: List<Chapter> = emptyList()
    val currentChapterName: MutableState<String> = mutableStateOf("")
    val mangaNameParam = savedStateHandle.get<String>(Constants.MANGA_NAME_PARAM).toString()
    var currentChapterIndex = savedStateHandle.get<String>(Constants.CURRENT_CHAPTER_PARAM)?.toInt() ?: 0
    val headers: MutableState<Headers> = mutableStateOf(Headers.headersOf("accept",""))

    val isUpdatingChapter: MutableState<Boolean> = mutableStateOf(false)
    val chapterHasBeenUpdated: MutableState<Boolean> = mutableStateOf(false)
    val isNext: MutableState<Boolean> = mutableStateOf(false)

    val pages: MutableState<List<String>> = mutableStateOf(emptyList())
    val mangaRepository = mangaRepositoryMap[providerParam]?: throw java.lang.IllegalArgumentException("No dependency found for : $providerParam")
    var pageImageDatas: MutableState<List<MutableState<PageImageData>>> = mutableStateOf(emptyList())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val error: MutableState<String> = mutableStateOf("")

    val isScrollEnabled: MutableState<Boolean> = mutableStateOf(true)

    var isFirst = true

    val isAppBarsVisible: MutableState<Boolean> = mutableStateOf(false)

    var composablesImages: List<@Composable () -> Unit> = emptyList()

    var currentPage: MutableState<Int> = mutableStateOf(1)

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
                    if (isFirst){
                        setupPageList()
                        isFirst = false
                    }
                    else{
                        nextPageLoad()
                    }
                }
                is Resource.Error -> {
                    error.value = result.message ?: "Unknown error"
                    isLoading.value = false
                }
                is Resource.Loading -> {
                    isLoading.value = true
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun setupPageList(){
        pageList = arrayListOf()
        pages.value.forEach(){ pageList.add(PageImageData(mutableStateOf(0f), mutableStateOf(0f), mutableStateOf(1f), mutableStateOf(Size.Zero))) }

        if (currentChapterIndex == chaptersParam.size - 1) {
            pageUnitList.add(@Composable { MangaFirstPage() })
        }
        else{
            pageUnitList.add(@Composable { MangaPreviousChapterPage(currentChapter = chaptersParam[currentChapterIndex].name, previousChapter = chaptersParam[currentChapterIndex+1].name) })
        }

        pages.value.forEachIndexed{ index, item -> pageUnitList.add(@Composable { MangaPageImage(url = item, pageImageData = pageList[index]) }) }

        if (currentChapterIndex == 0){
            pageUnitList.add(@Composable { MangaLastPage()})
        }
        else{
            pageUnitList.add(@Composable { MangaNextChapterPage(finishedChapter = chaptersParam[currentChapterIndex].name, nextChapter = chaptersParam[currentChapterIndex-1].name) })
        }
        currentChapterName.value = chaptersParam[currentChapterIndex].name
    }
    //TEST
    fun nextPageSetup(){
        isNext.value = true
        isUpdatingChapter.value = true
        if (currentChapterIndex > 0){
            currentChapterIndex -= 1
            pages.value = emptyList()
            getChapter(chaptersParam[currentChapterIndex].chapterUrl)
        }
        else{
            isUpdatingChapter.value = false
        }
    }
    fun nextPageLoad(){
        pageList = arrayListOf()
        pages.value.forEach(){ pageList.add(PageImageData(mutableStateOf(0f), mutableStateOf(0f), mutableStateOf(1f), mutableStateOf(Size.Zero))) }
        pageUnitList = arrayListOf()
        if (currentChapterIndex == chaptersParam.size - 1) {
            pageUnitList.add(@Composable { MangaFirstPage() })
        }
        else{
            pageUnitList.add(@Composable { MangaPreviousChapterPage(currentChapter = chaptersParam[currentChapterIndex].name, previousChapter = chaptersParam[currentChapterIndex+1].name) })
        }

        pages.value.forEachIndexed{ index, item -> pageUnitList.add(@Composable { MangaPageImage(url = item, pageImageData = pageList[index]) }) }

        if (currentChapterIndex == 0){
            pageUnitList.add(@Composable { MangaLastPage()})
        }
        else{
            pageUnitList.add(@Composable { MangaNextChapterPage(finishedChapter = chaptersParam[currentChapterIndex].name, nextChapter = chaptersParam[currentChapterIndex-1].name) })
        }
        currentChapterName.value = chaptersParam[currentChapterIndex].name
        chapterHasBeenUpdated.value = true
    }

    fun previousPageSetup(){
        isNext.value = false
        isUpdatingChapter.value = true
        if (currentChapterIndex < chaptersParam.size - 1){
            currentChapterIndex += 1
            pages.value = emptyList()
            getChapter(chaptersParam[currentChapterIndex].chapterUrl)
        }
        else{
            isUpdatingChapter.value = false
        }
    }
}

data class PageImageData(var offsetX: MutableState<Float>, var offsetY: MutableState<Float>, var scaleFactor: MutableState<Float>, var imageSize: MutableState<Size>)