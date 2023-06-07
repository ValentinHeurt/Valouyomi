package com.example.valouyomi.presentation.manga_reader

import android.content.res.Configuration
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.valouyomi.presentation.manga_details.MangaViewModel
import kotlinx.coroutines.coroutineScope
import retrofit2.http.Headers
import kotlin.math.pow
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MangaReaderScreen(
    navController: NavController,
    viewModel: MangaReaderViewModel = hiltViewModel()
){
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var doubleClickPosition by remember { mutableStateOf(IntOffset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offsetX += offsetChange.x / scale
        offsetY += offsetChange.y / scale
    }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val pages = viewModel.pages.value

    val isScrollEnabled = remember { mutableStateOf(true)}
    val pagerState = rememberPagerState()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)){
        VerticalPager(
            state = pagerState,
            userScrollEnabled = isScrollEnabled.value,
            pageCount = pages.count(),
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, dou ->
                        zoom?.let {
                            if ((scale * it <= 0.75) || (scale * it >= 3)) else scale *= it
                        }
                        pan?.let {
                            if (scale != 1f) {
                                offsetX += it.x / scale
                                offsetY += it.y / scale
                            }
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { position ->
                            println("ouioui")
                            if (scale == 1f) {
                                scale = 2f
                                isScrollEnabled.value = false;
                            } else {
                                scale = 1f
                                isScrollEnabled.value = true;
                            }
                            //doubleClickPosition = IntOffset(position.x.roundToInt(), position.y.roundToInt())
                            offsetX = calculateTargetOffsetX(position.x, scale)
                            offsetY = calculateTargetOffsetY(position.y, scale)
                        }
                    )
                }
                .scrollable(
                    state = pagerState,
                    enabled = false,
                    orientation = Orientation.Vertical
                )
        ) {
            val imageRequest = ImageRequest.Builder(LocalContext.current)
                .data(pages[it])
                .headers(viewModel.headers.value)
                .build()
            val painter = rememberAsyncImagePainter(model = imageRequest)
            Image(
                painter = painter, contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    ),
                contentScale = ContentScale.Fit,
            )
            if (painter.state is AsyncImagePainter.State.Loading) CircularProgressIndicator()
        }
    }
}
private fun calculateTargetOffsetX(doubleClickX: Float, targetScale: Float): Float {
    // Calculate the target offset X based on the double click position and target scale
    return doubleClickX * (1 - targetScale)
}

private fun calculateTargetOffsetY(doubleClickY: Float, targetScale: Float): Float {
    // Calculate the target offset Y based on the double click position and target scale
    return doubleClickY * (1 - targetScale)
}