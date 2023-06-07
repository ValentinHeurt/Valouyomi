package com.example.valouyomi.presentation.manga_reader

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter

@Composable
fun ZoomableImage(
    image: AsyncImagePainter,
    modifier: Modifier = Modifier
){
    Image(
        painter = image, contentDescription = "",
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Fit
    )
}