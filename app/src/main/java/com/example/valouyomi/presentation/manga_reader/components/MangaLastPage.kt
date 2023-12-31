package com.example.valouyomi.presentation.manga_reader.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MangaLastPage(){
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center){
        Text(text = "C'était le dernier chapitre", color = Color.White)
    }
}