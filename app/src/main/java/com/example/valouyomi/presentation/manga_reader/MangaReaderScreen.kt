package com.example.valouyomi.presentation.manga_reader

import android.content.Context
import android.content.res.Configuration
import android.media.Image
import android.util.DisplayMetrics
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
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.valouyomi.presentation.manga_details.MangaViewModel
import kotlinx.coroutines.coroutineScope
import retrofit2.http.Headers
import kotlin.math.absoluteValue
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

    var boxWidth by remember { mutableStateOf(0) }
    var boxHeight by remember { mutableStateOf(0) }

    var imageSize by remember { mutableStateOf(Size.Zero) }

    var doubleClickPosition by remember { mutableStateOf(IntOffset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offsetX += offsetChange.x / scale
        offsetY += offsetChange.y / scale
    }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val pages = viewModel.pages.value


    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp


    val isScrollEnabled = remember { mutableStateOf(true)}
    val pagerState = rememberPagerState()
    Box(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned { coordinates ->
            boxWidth = coordinates.size.width
            boxHeight = coordinates.size.height
        }
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
                            val imageHeight = imageSize.height
                            val imageWidth  = imageSize.width

                            val maxYOffset = (((imageHeight * scale) - boxHeight) / 2).coerceAtLeast(0f)
                            val maxXOffset = (((imageWidth * scale) - boxWidth) / 2).coerceAtLeast(0f)
                            if (scale != 1f) {

                                offsetX += it.x / scale
                                offsetY += it.y / scale
                                offsetX = offsetX.coerceIn(-maxXOffset, maxXOffset)
                                offsetY = offsetY.coerceIn(-maxYOffset, maxYOffset)
                                println("scale : $scale maxY : $maxYOffset offsetY : $offsetY")
                            }
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { position ->
                            if (scale == 1f) {
                                scale = 2f
                                isScrollEnabled.value = false;
                            } else {
                                scale = 1f
                                isScrollEnabled.value = true;
                            }
                            //doubleClickPosition = IntOffset(position.x.roundToInt(), position.y.roundToInt())
                            val imageHeight = imageSize.height
                            val imageWidth  = imageSize.width

                            val maxYOffset = (((imageHeight * scale) - boxHeight) / 2).coerceAtLeast(0f)
                            println("imageHeight : $imageHeight BoxHeight : $boxHeight  max : $maxYOffset scale : $scale imageWidth : $imageWidth")
                            if (scale != 1f) {

                                offsetX = ((boxWidth / 2) - position.x)
                                offsetY = ((boxHeight / 2) - position.y).coerceIn(-maxYOffset,maxYOffset)
                            }
                            else{
                                offsetX = 0f
                                offsetY = 0f
                            }


                            println("x off : " +offsetX.toString())
                            println("y off : " +offsetY.toString())
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
            val content = ContentScale.Fit
            if (painter.state is AsyncImagePainter.State.Success){
                val scaleFactor = content.computeScaleFactor(painter.intrinsicSize, Size(boxWidth.toFloat(), boxHeight.toFloat()))
                imageSize = scaleFactor.times(painter.intrinsicSize)
            }
            if (painter.state is AsyncImagePainter.State.Loading) CircularProgressIndicator()
        }
    }
}