package com.example.valouyomi.presentation.settings

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel : SettingsViewModel = hiltViewModel()
){
    Text(text = "ParametersScreen")
}