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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.valouyomi.presentation.manga_reader.components.MangaPageImage

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MangaReaderScreen(
    navController: NavController,
    viewModel: MangaReaderViewModel = hiltViewModel()
){
    var imageSize by remember { mutableStateOf(Size.Zero) }
    val pages = viewModel.composablesImages
    val pagerState = rememberPagerState()
    var doubleClickPosition by remember { mutableStateOf(IntOffset.Zero) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    var currentMangaPageImage: @Composable () -> Unit = {}


    Box(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned { coordinates ->
            viewModel.boxWidth.value = coordinates.size.width
            viewModel.boxHeight.value = coordinates.size.height
        }
        .background(Color.Black)){
        VerticalPager(
            state = pagerState,
            userScrollEnabled = viewModel.isScrollEnabled.value,
            pageCount = viewModel.pages.value.size,
            modifier = Modifier
                .scrollable(
                    state = pagerState,
                    enabled = false,
                    orientation = Orientation.Vertical
                )
        ) {
            MangaPageImage(url = viewModel.pages.value[it])
        }
    }
}