package com.example.valouyomi.presentation.manga_reader

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.media.Image
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.WindowManager
import android.widget.HorizontalScrollView
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
import com.example.valouyomi.presentation.manga_reader.components.MangaPageImage
import com.example.valouyomi.presentation.manga_reader.components.MangaReaderTopInfo
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@SuppressLint("UnrememberedMutableState", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MangaReaderScreen(
    navController: NavController,
    viewModel: MangaReaderViewModel = hiltViewModel()
){
    viewModel.chaptersParam = navController.previousBackStackEntry?.savedStateHandle?.get<List<Chapter>>("chapters")
    var imageSize by remember { mutableStateOf(Size.Zero) }
    val pages = viewModel.composablesImages
    val pagerState = rememberPagerState()
    var doubleClickPosition by remember { mutableStateOf(IntOffset.Zero) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    var triggerState by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    var scrollOffset by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var canScrollBackward = mutableStateOf(true)
    var oldPage by remember { mutableStateOf(0) }

    viewModel.isScrollEnabled.value = ((viewModel.pageList.getOrNull(pagerState.currentPage)?.scaleFactor?.value ?: 1f) == 1f)
    if (pagerState.currentPage == 2) canScrollBackward.value = false
    Scaffold(topBar = { MangaReaderTopInfo() }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                viewModel.boxWidth.value = coordinates.size.width
                viewModel.boxHeight.value = coordinates.size.height
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { position ->
                        val currentPage =
                            viewModel.pageList.getOrNull(pagerState.currentPage) ?: PageImageData(
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
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, dou ->
                    val currentPage =
                        viewModel.pageList.getOrNull(pagerState.currentPage) ?: PageImageData(
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
            .pointerInput(Unit) {
                detectDragGestures(onDragStart = {
                    isDragging = true
                    oldPage = pagerState.currentPage
                    println("oldpage : $oldPage")
                }, onDragEnd = {
                    isDragging = false
                    if (scrollOffset < 600f) {

                        coroutineScope.launch {
                            pagerState.scrollBy(-scrollOffset)
                            scrollOffset = 0f
                            println("oldpage : $oldPage after")
                        }
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(oldPage + 1)
                            println("oldpage :" + (oldPage + 1))
                        }
                    }
                }, onDrag = { change, dragAmount ->
                    val currentPage =
                        viewModel.pageList.getOrNull(pagerState.currentPage) ?: PageImageData(
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
                        var isAlreadyMaxDown = false
                        var isMoreThanMaxDown = false
                        if (currentPage.offsetY.value == -maxYOffset) isAlreadyMaxDown =
                            true

                        currentPage.offsetX.value += dragAmount.x / currentPage.scaleFactor.value
                        currentPage.offsetY.value += dragAmount.y / currentPage.scaleFactor.value
                        if (currentPage.offsetY.value < -maxYOffset) isMoreThanMaxDown = true
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

                        if (isAlreadyMaxDown && isMoreThanMaxDown && isDragging) {
                            if (pagerState.currentPage < viewModel.pages.value.size - 1) {
                                triggerState++
                                coroutineScope.launch {
                                    pagerState.scrollBy(dragAmount.y.absoluteValue)
                                    scrollOffset += dragAmount.y.absoluteValue
                                }
                            }

                        }
                    }
                })
            }
            .background(Color.Black)){
            VerticalPager(
                state = pagerState,
                userScrollEnabled = viewModel.isScrollEnabled.value,
                pageCount = viewModel.pages.value.size,
                beyondBoundsPageCount = 10,
                modifier = Modifier
                    .scrollable(
                        state = pagerState,
                        enabled = false,
                        orientation = Orientation.Vertical
                    )
            ) {
                viewModel.pageUnitList.getOrNull(it)?.invoke()
            }
        }
    }


}