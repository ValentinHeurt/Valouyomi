package com.example.valouyomi.presentation.manga_search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.valouyomi.presentation.manga_search.MangaSearchViewModel

@Composable
fun FiltersBottomSheet(
    viewModel: MangaSearchViewModel = hiltViewModel()
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .background(Color.Gray)
    ){
        LazyColumn(){
            items(viewModel.genresState.value.genres.toList()){
                val genre = it.first
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = it.first, color = Color.White)
                    Checkbox(checked = it.second, onCheckedChange = { viewModel.genresState.value.genres[genre] = it })
                }
            }
        }
    }
}