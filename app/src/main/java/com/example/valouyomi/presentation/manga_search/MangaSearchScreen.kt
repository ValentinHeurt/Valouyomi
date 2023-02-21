package com.example.valouyomi.presentation.manga_search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.valouyomi.domain.models.MangaThumbnail
import com.example.valouyomi.presentation.Screen
import com.example.valouyomi.presentation.components.MangaThumbnailCard
import com.example.valouyomi.presentation.manga_search.components.MangaSearchTopBar

@Composable
fun MangaSearchScreen(
    navController: NavController,
    viewModel: MangaSearchViewModel = hiltViewModel()
){

    val genresState = viewModel.genresState.value
    val thumbnails by viewModel.mangaThumbnails.observeAsState(initial = emptyList())
    Column(modifier = Modifier.fillMaxSize()){
        MangaSearchTopBar(providerName = viewModel.param)
        Box(modifier = Modifier.fillMaxSize()){
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ){
                items(thumbnails){ mangaThumbnail ->
                    if (thumbnails.indexOf(mangaThumbnail) == thumbnails.lastIndex){
                        viewModel.addMangaThumbnails()
                    }
                    MangaThumbnailCard(
                        mangaThumbnail = mangaThumbnail,
                        onItemClicked = {
                            navController.navigate(Screen.MangaSearchScreen.route + "/${mangaThumbnail.url}")
                        })
                }
            }
            if (viewModel.error.value.isNotBlank()){
                Text(
                    text = viewModel.error.value,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(alignment = Alignment.Center)
                )
            }
            if(viewModel.isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

}