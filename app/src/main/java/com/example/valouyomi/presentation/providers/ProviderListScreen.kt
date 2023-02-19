package com.example.valouyomi.presentation.providers

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.valouyomi.presentation.Screen
import com.example.valouyomi.presentation.components.MangaProviderItem
import com.example.valouyomi.presentation.components.MangaThumbnailCard
import com.example.valouyomi.presentation.manga_search.MangaSearchViewModel

@Composable
fun ProviderListScreen(
    navController: NavController,
    viewModel: ProviderListViewModel = hiltViewModel()
){
    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(viewModel.providers){ provider ->
                MangaProviderItem(
                    mangaProvider = provider,
                    onItemClicked = {
                        navController.navigate(provider.route + "/${provider.providerName}")
                    }
                )
            }
        }
    }

}