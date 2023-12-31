package com.example.valouyomi.presentation.manga_reader.components

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.valouyomi.presentation.manga_reader.MangaReaderViewModel
import com.example.valouyomi.presentation.manga_reader.PageImageData

@Composable
fun MangaReaderBottomInfo(
    modifier: Modifier,
    onValueChange: (Float) -> Unit,
    viewModel: MangaReaderViewModel = hiltViewModel()
){
    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .alpha(0.75f)
            .padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Gray),
                onClick = { /*TODO*/ }
            ) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "backChapter")
            }
            Slider(
                modifier = Modifier
                    .background(Color.Gray, shape = RoundedCornerShape(20.dp))
                    .padding(start = 10.dp, end = 10.dp)
                    .weight(1f),
                value = viewModel.currentPage.value.toFloat(),
                onValueChange = onValueChange,
                steps = viewModel.pageList.size,
                valueRange = if(viewModel.pageList.size > 0) 1f..viewModel.pageList.size.toFloat() else 1f..10f
            )
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Gray),
                onClick = { /*TODO*/ }
            ) {
                Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = "nextChapter")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        BottomAppBar(actions = {})
    }
}