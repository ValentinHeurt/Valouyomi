package com.example.valouyomi.presentation.library

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun LibraryScreen(
    navController: NavController,
    viewModel : LibraryViewModel = hiltViewModel()
){
    Text("LibraryScreen")
}