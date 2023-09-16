package com.example.valouyomi.presentation.manga_reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.valouyomi.presentation.manga_reader.MangaReaderViewModel
import com.example.valouyomi.presentation.manga_reader.PageImageData

@Composable
fun MangaReaderTopInfo(
    viewModel: MangaReaderViewModel = hiltViewModel()
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Gray)){
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription ="Back" )
            Text(text = viewModel.mangaNameParam)
            viewModel.currentChapterIndex?.let { viewModel.chaptersParam?.get(it)?.let { Text(text = it.name) } }
        }
    }
}