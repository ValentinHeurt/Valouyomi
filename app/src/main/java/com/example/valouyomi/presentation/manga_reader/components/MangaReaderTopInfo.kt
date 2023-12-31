package com.example.valouyomi.presentation.manga_reader.components

import android.content.Context
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.valouyomi.presentation.manga_reader.MangaReaderViewModel
import com.example.valouyomi.presentation.manga_reader.PageImageData

@Composable
fun MangaReaderTopInfo(
    modifier: Modifier,
    viewModel: MangaReaderViewModel = hiltViewModel()
){
    TopAppBar(
        backgroundColor = Color.Gray,
        elevation = 0.dp,
        navigationIcon = {BackAction()},
        actions = {

        },
        title = { Column(modifier = Modifier
            .padding(5.dp)) {
            Text(text = viewModel.mangaNameParam, maxLines = 1, overflow =  TextOverflow.Ellipsis, fontSize = 15.sp)
            Text(text = viewModel.chaptersParam[viewModel.currentChapterIndex].name, maxLines = 1, overflow =  TextOverflow.Ellipsis, fontSize = 12.sp)
        }},
        modifier = modifier
    )
}

@Composable
fun BackAction(){
    val localOnBackPressed = LocalOnBackPressedDispatcherOwner.current

    IconButton(onClick = { BackButtonClicked(localOnBackPressed) }) {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription ="Back" )
    }
}

fun BackButtonClicked(localOnBackPressed: OnBackPressedDispatcherOwner? ){
    localOnBackPressed?.onBackPressedDispatcher?.onBackPressed()
}