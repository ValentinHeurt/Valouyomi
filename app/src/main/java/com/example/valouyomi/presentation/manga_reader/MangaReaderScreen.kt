package com.example.valouyomi.presentation.manga_reader

import android.widget.HorizontalScrollView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.valouyomi.presentation.manga_details.MangaViewModel
import retrofit2.http.Headers

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MangaReaderScreen(
    navController: NavController,
    viewModel: MangaReaderViewModel = hiltViewModel()
){
    val pages = viewModel.pages.value
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)){
        VerticalPager(pageCount = pages.count()) {
            val imageRequest = ImageRequest.Builder(LocalContext.current)
                .data(pages[it])
                .headers(viewModel.headers.value)
                .build()
            val painter = rememberAsyncImagePainter(model = imageRequest)
            Image(
                painter = painter, contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}