package com.example.valouyomi.presentation.manga_search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.valouyomi.presentation.manga_search.MangaSearchState

@Composable
fun MangaSearchTopBar(
    state: MangaSearchState
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = Color.Gray)
    ){
        Icon(Icons.Default.ArrowBack, contentDescription = "")

    }
}