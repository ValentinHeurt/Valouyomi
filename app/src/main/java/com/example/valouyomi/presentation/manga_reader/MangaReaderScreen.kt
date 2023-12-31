package com.example.valouyomi.presentation.manga_reader

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.media.Image
import android.util.DisplayMetrics
import android.util.Log
import android.view.GestureDetector
import android.view.WindowManager
import android.widget.HorizontalScrollView
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.times
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.valouyomi.domain.models.Chapter
import com.example.valouyomi.presentation.Screen
import com.example.valouyomi.presentation.manga_reader.components.MangaPageImage
import com.example.valouyomi.presentation.manga_reader.components.MangaReaderBottomInfo
import com.example.valouyomi.presentation.manga_reader.components.MangaReaderTopInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.absoluteValue

@SuppressLint("UnrememberedMutableState", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MangaReaderScreen(
    navController: NavController,
    viewModel: MangaReaderViewModel = hiltViewModel()
){
    viewModel.chaptersParam = navController.previousBackStackEntry?.savedStateHandle?.get<List<Chapter>>("chapters") ?: emptyList()
    var imageSize by remember { mutableStateOf(Size.Zero) }
    val pages = viewModel.composablesImages
    val pagerState = rememberPagerState(initialPage = 1)
    var doubleClickPosition by remember { mutableStateOf(IntOffset.Zero) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    var triggerState by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    var scrollOffset by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var canScrollBackward = mutableStateOf(true)
    var hasPageChangeTrigger by remember { mutableStateOf(false)}
    var oldPage by remember { mutableStateOf(0) }


    viewModel.isScrollEnabled.value = ((viewModel.pageList.getOrNull(pagerState.currentPage-1)?.scaleFactor?.value ?: 1f) == 1f)
    viewModel.currentPage.value = if (pagerState.currentPage == 0) 1 else pagerState.currentPage
    println("current page vm "  + viewModel.currentPage.value)
    println("size "  + viewModel.pageList.size)
    if (pagerState.isScrollInProgress && pagerState.currentPage == viewModel.pageUnitList.size-1 && !viewModel.isUpdatingChapter.value){
        viewModel.nextPageSetup()
    }
    //Scaffold(topBar = { MangaReaderTopInfo() }) {
    Box(modifier = Modifier
        .fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                viewModel.boxWidth.value = coordinates.size.width
                viewModel.boxHeight.value = coordinates.size.height
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        viewModel.isAppBarsVisible.value = !viewModel.isAppBarsVisible.value
                    },
                    onDoubleTap = { position ->
                        if (pagerState.currentPage != 0 && pagerState.currentPage != viewModel.pageUnitList.size) {

                            val currentPage =
                                viewModel.pageList.getOrNull(pagerState.currentPage - 1)
                                    ?: PageImageData(
                                        mutableStateOf(0f),
                                        mutableStateOf(0f),
                                        mutableStateOf(1f),
                                        mutableStateOf(Size.Zero)
                                    )
                            if (currentPage.scaleFactor.value == 1f) {
                                currentPage.scaleFactor.value = 2f
                                viewModel.isScrollEnabled.value = false;
                            } else {
                                currentPage.scaleFactor.value = 1f
                                viewModel.isScrollEnabled.value = true;
                            }
                            //doubleClickPosition = IntOffset(position.x.roundToInt(), position.y.roundToInt())
                            val imageHeight =
                                currentPage.imageSize.value.height
                            val imageWidth =
                                currentPage.imageSize.value.width

                            val maxYOffset =
                                (((imageHeight * currentPage.scaleFactor.value) - viewModel.boxHeight.value) / 2).coerceAtLeast(
                                    0f
                                )
                            if (currentPage.scaleFactor.value != 1f) {

                                currentPage.offsetX.value =
                                    ((viewModel.boxWidth.value / 2) - position.x)
                                currentPage.offsetY.value =
                                    ((viewModel.boxHeight.value / 2) - position.y).coerceIn(
                                        -maxYOffset,
                                        maxYOffset
                                    )
                            } else {
                                currentPage.offsetX.value = 0f
                                currentPage.offsetY.value = 0f
                            }
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, dou ->
                    if (pagerState.currentPage != 0 && pagerState.currentPage != viewModel.pageUnitList.size) {
                        val currentPage =
                            viewModel.pageList.getOrNull(pagerState.currentPage - 1)
                                ?: PageImageData(
                                    mutableStateOf(0f),
                                    mutableStateOf(0f),
                                    mutableStateOf(1f),
                                    mutableStateOf(Size.Zero)
                                )
                        zoom?.let {
                            if ((currentPage.scaleFactor.value * it <= 0.75) || (currentPage.scaleFactor.value * it >= 3)) else currentPage.scaleFactor.value *= it
                        }
                        pan?.let {

                        }
                    }
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(onDragStart = {
                    isDragging = true
                    oldPage = pagerState.currentPage
                    scrollOffset = 0f
                }, onDragEnd = {
                    isDragging = false
                    if (scrollOffset > 0f) {
                        if (scrollOffset < 600f) {
                            coroutineScope.launch {
                                pagerState.scrollBy(-scrollOffset)
                                scrollOffset = 0f
                            }
                        } else {
                            coroutineScope.launch {
                                val currentPage =
                                    viewModel.pageList.getOrNull(pagerState.currentPage - 1)
                                        ?: PageImageData(
                                            mutableStateOf(0f),
                                            mutableStateOf(0f),
                                            mutableStateOf(1f),
                                            mutableStateOf(Size.Zero)
                                        )
                                currentPage.scaleFactor.value = 1f
                                currentPage.offsetX.value = 0f
                                currentPage.offsetY.value = 0f
                                pagerState.animateScrollToPage(oldPage + 1)
                            }
                        }
                    } else {
                        if (scrollOffset > -600f) {
                            coroutineScope.launch {
                                pagerState.scrollBy(scrollOffset.absoluteValue)
                                scrollOffset = 0f
                            }
                        } else {
                            coroutineScope.launch {
                                val currentPage =
                                    viewModel.pageList.getOrNull(pagerState.currentPage - 1)
                                        ?: PageImageData(
                                            mutableStateOf(0f),
                                            mutableStateOf(0f),
                                            mutableStateOf(1f),
                                            mutableStateOf(Size.Zero)
                                        )
                                currentPage.scaleFactor.value = 1f
                                currentPage.offsetX.value = 0f
                                currentPage.offsetY.value = 0f
                                pagerState.animateScrollToPage(oldPage - 1)
                            }
                        }
                    }

                    hasPageChangeTrigger = false
                }, onDrag = { change, dragAmount ->
                    val currentPage =
                        viewModel.pageList.getOrNull(pagerState.currentPage - 1) ?: PageImageData(
                            mutableStateOf(0f),
                            mutableStateOf(0f),
                            mutableStateOf(1f),
                            mutableStateOf(Size.Zero)
                        )
                    val imageHeight =
                        currentPage.imageSize.value.height
                    val imageWidth =
                        currentPage.imageSize.value.width

                    val maxYOffset =
                        (((imageHeight * currentPage.scaleFactor.value) - viewModel.boxHeight.value) / 2).coerceAtLeast(
                            0f
                        )
                    val maxXOffset =
                        (((imageWidth * currentPage.scaleFactor.value) - viewModel.boxWidth.value) / 2).coerceAtLeast(
                            0f
                        )
                    if (currentPage.scaleFactor.value != 1f) {
                        var isMoreThanMaxDown = false
                        var isMoreThanMaxUp = false

                        currentPage.offsetX.value += dragAmount.x / currentPage.scaleFactor.value
                        currentPage.offsetY.value += dragAmount.y / currentPage.scaleFactor.value

                        if (currentPage.offsetY.value < -maxYOffset + scrollOffset) isMoreThanMaxDown =
                            true
                        if (currentPage.offsetY.value > maxYOffset + scrollOffset) isMoreThanMaxUp =
                            true
                        currentPage.offsetX.value =
                            currentPage.offsetX.value.coerceIn(
                                -maxXOffset,
                                maxXOffset
                            )
                        currentPage.offsetY.value =
                            currentPage.offsetY.value.coerceIn(
                                -maxYOffset,
                                maxYOffset
                            )

                        if (isMoreThanMaxUp && isDragging) {
                            if (pagerState.currentPage > 1) {
                                if (scrollOffset - dragAmount.y.absoluteValue >= -1000) {
                                    coroutineScope.launch {
                                        if (dragAmount.y < 0) {
                                            pagerState.scroll {
                                                scrollBy(dragAmount.y.absoluteValue)
                                            }
                                            //pagerState.scrollBy(dragAmount.y.absoluteValue)
                                            scrollOffset += dragAmount.y.absoluteValue
                                        } else {
                                            pagerState.scroll {
                                                scrollBy(-dragAmount.y)
                                            }
                                            //pagerState.scrollBy(dragAmount.y.absoluteValue)
                                            scrollOffset -= dragAmount.y.absoluteValue
                                        }
                                    }
                                } else {
                                    coroutineScope.launch {
                                        val currentPage =
                                            viewModel.pageList.getOrNull(pagerState.currentPage - 1)
                                                ?: PageImageData(
                                                    mutableStateOf(0f),
                                                    mutableStateOf(0f),
                                                    mutableStateOf(1f),
                                                    mutableStateOf(Size.Zero)
                                                )
                                        currentPage.scaleFactor.value = 1f
                                        currentPage.offsetX.value = 0f
                                        currentPage.offsetY.value = 0f
                                        pagerState.animateScrollToPage(oldPage - 1)
                                    }
                                }
                            }
                        }

                        if (isMoreThanMaxDown && isDragging) {
                            if (pagerState.currentPage - 1 < viewModel.pages.value.size - 1) {
                                triggerState++
                                if (scrollOffset + dragAmount.y.absoluteValue <= 1000) {
                                    coroutineScope.launch {
                                        if (dragAmount.y < 0) {
                                            pagerState.scroll {
                                                scrollBy(dragAmount.y.absoluteValue)
                                            }
                                            //pagerState.scrollBy(dragAmount.y.absoluteValue)
                                            scrollOffset += dragAmount.y.absoluteValue
                                        } else {
                                            pagerState.scroll {
                                                scrollBy(-dragAmount.y)
                                            }
                                            //pagerState.scrollBy(dragAmount.y.absoluteValue)
                                            scrollOffset -= dragAmount.y.absoluteValue
                                        }
                                    }
                                } else {
                                    coroutineScope.launch {
                                        val currentPage =
                                            viewModel.pageList.getOrNull(pagerState.currentPage - 1)
                                                ?: PageImageData(
                                                    mutableStateOf(0f),
                                                    mutableStateOf(0f),
                                                    mutableStateOf(1f),
                                                    mutableStateOf(Size.Zero)
                                                )
                                        currentPage.scaleFactor.value = 1f
                                        currentPage.offsetX.value = 0f
                                        currentPage.offsetY.value = 0f
                                        pagerState.animateScrollToPage(oldPage + 1)
                                    }
                                }
                            }
                        }
                    }
                })
            }
            .background(Color.Black)) {
            VerticalPager(
                state = pagerState,
                userScrollEnabled = viewModel.isScrollEnabled.value,
                pageCount = viewModel.pageUnitList.size,
                beyondBoundsPageCount = 10,
                modifier = Modifier
                    .scrollable(
                        state = pagerState,
                        enabled = false,
                        orientation = Orientation.Vertical
                    )
            ) {
                println(it)
                viewModel.pageUnitList.getOrNull(it)?.invoke()
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .scale(if (viewModel.isAppBarsVisible.value) 1f else 0f)){
            MangaReaderTopInfo(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp))
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .scale(if (viewModel.isAppBarsVisible.value) 1f else 0f)){
            MangaReaderBottomInfo(modifier = Modifier, onValueChange = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(if (it == 0f) it.toInt()+1 else it.toInt())
                }
            })
        }
        

    }
    }

@OptIn(ExperimentalFoundationApi::class)
fun ScrollToPage(coroutineScope: CoroutineScope, pagerState: PagerState, page: Int){
    coroutineScope.launch {
        pagerState.scrollToPage(page)
    }
}

//}