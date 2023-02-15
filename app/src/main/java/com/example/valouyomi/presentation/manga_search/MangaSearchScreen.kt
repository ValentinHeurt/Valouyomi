package com.example.valouyomi.presentation.manga_search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.valouyomi.presentation.Screen
import com.example.valouyomi.presentation.components.MangaThumbnailCard

@Composable
fun MangaSearchScreen(
    navController: NavController,
    viewModel: MangaSearchViewModel = hiltViewModel()
){
    val mangaThumbnailsState = viewModel.mangaThumbnailsState.value
    val genresState = viewModel.genresState.value

    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(mangaThumbnailsState.mangaThumbnails){ mangaThumbnail ->
                MangaThumbnailCard(
                    mangaThumbnail = mangaThumbnail,
                    onItemClicked = {
                        navController.navigate(Screen.MangaSearchScreen.route + "/${mangaThumbnail.url}")
                    })
            }
        }
        if (mangaThumbnailsState.error.isNotBlank()){
            Text(
                text = mangaThumbnailsState.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }
        if(mangaThumbnailsState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

}