package com.example.valouyomi.presentation.manga_reader.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.times
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.valouyomi.presentation.manga_reader.MangaReaderViewModel
import com.example.valouyomi.presentation.manga_reader.PageImageData

@Composable
fun MangaPageImage(
    url: String,
    viewModel: MangaReaderViewModel = hiltViewModel()
){
    var scaleFactor by remember { mutableStateOf(1f)}
    var offsetX by remember { mutableStateOf(0f)}
    var offsetY by remember { mutableStateOf(0f)}
    var imageSize = remember { mutableStateOf(Size.Zero) }

    val pageModifier = Modifier
        .fillMaxSize()
        .graphicsLayer(
            scaleX = scaleFactor,
            scaleY = scaleFactor,
            translationX = offsetX,
            translationY = offsetY
        ).pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = { position ->
                    if (scaleFactor == 1f) {
                        scaleFactor = 2f
                        viewModel.isScrollEnabled.value = true;
                    } else {
                        scaleFactor = 1f
                        viewModel.isScrollEnabled.value = true;
                    }
                    //doubleClickPosition = IntOffset(position.x.roundToInt(), position.y.roundToInt())
                    val imageHeight = imageSize.value.height
                    val imageWidth = imageSize.value.width

                    val maxYOffset =
                        (((imageHeight * scaleFactor) - viewModel.boxHeight.value) / 2).coerceAtLeast(0f)
                    println("imageHeight : $imageHeight viewModel.boxHeight.value : $viewModel.boxHeight.value  max : $maxYOffset scale : $scaleFactor.value imageWidth : $imageWidth")
                    if (scaleFactor != 1f) {

                        offsetX = ((viewModel.boxWidth.value / 2) - position.x)
                        offsetY = ((viewModel.boxHeight.value / 2) - position.y).coerceIn(-maxYOffset, maxYOffset)
                    } else {
                        offsetX = 0f
                        offsetY = 0f
                    }
                }
            )
        }.pointerInput(Unit) {
            detectTransformGestures { centroid, pan, zoom, dou ->
                zoom?.let {
                    if ((scaleFactor * it <= 0.75) || (scaleFactor * it >= 3)) else scaleFactor *= it
                }
                pan?.let {
                    val imageHeight = imageSize.value.height
                    val imageWidth  = imageSize.value.width

                    val maxYOffset = (((imageHeight * scaleFactor) - viewModel.boxHeight.value) / 2).coerceAtLeast(0f)
                    val maxXOffset = (((imageWidth * scaleFactor) - viewModel.boxWidth.value) / 2).coerceAtLeast(0f)
                    if (scaleFactor != 1f) {

                        offsetX += it.x / scaleFactor
                        offsetY += it.y / scaleFactor
                        offsetX = offsetX.coerceIn(-maxXOffset, maxXOffset)
                        offsetY = offsetY.coerceIn(-maxYOffset, maxYOffset)
                        println("scale : $scaleFactor maxY : $maxYOffset offsetY : $offsetY")
                    }
                }
            }
        }
    println("1")
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .headers(viewModel.headers.value)
        .build()
    val painter = rememberAsyncImagePainter(model = imageRequest)

    Image(
        painter = painter, contentDescription = "",
        modifier = pageModifier,
        contentScale = ContentScale.Fit,
    )
    val content = ContentScale.Fit
    println(painter.state)
    if (painter.state is AsyncImagePainter.State.Success){
        val scaleFactor = content.computeScaleFactor(painter.intrinsicSize, Size(viewModel.boxWidth.value.toFloat(), viewModel.boxHeight.value.toFloat()))
        imageSize.value = scaleFactor.times(painter.intrinsicSize)
    }
    if (painter.state is AsyncImagePainter.State.Loading) {
        CircularProgressIndicator()
        println("load")
    }
    println(painter.state)
    println("2")
}