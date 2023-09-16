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
    pageImageData: PageImageData,
    viewModel: MangaReaderViewModel = hiltViewModel()
){

    val pageModifier = Modifier
        .fillMaxSize()
        .graphicsLayer(
            scaleX = pageImageData.scaleFactor.value,
            scaleY = pageImageData.scaleFactor.value,
            translationX = pageImageData.offsetX.value,
            translationY = pageImageData.offsetY.value
        )
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
    if (painter.state is AsyncImagePainter.State.Success){
        val scaleFactor = content.computeScaleFactor(painter.intrinsicSize, Size(viewModel.boxWidth.value.toFloat(), viewModel.boxHeight.value.toFloat()))
        pageImageData.imageSize.value = scaleFactor.times(painter.intrinsicSize)
    }
    if (painter.state is AsyncImagePainter.State.Loading) {
        CircularProgressIndicator()
    }
}