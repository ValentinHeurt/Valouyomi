package com.example.valouyomi.presentation.manga_reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.valouyomi.presentation.manga_reader.MangaReaderViewModel
import com.example.valouyomi.presentation.manga_reader.PageImageData

@Composable
fun MangaReaderBottomInfo(
    pageImageData: PageImageData,
    viewModel: MangaReaderViewModel = hiltViewModel()
){
    Box(modifier = Modifier.fillMaxWidth().background(Color.Gray)){

    }

}