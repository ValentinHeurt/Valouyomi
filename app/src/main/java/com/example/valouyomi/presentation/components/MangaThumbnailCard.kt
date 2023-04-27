package com.example.valouyomi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.valouyomi.domain.models.MangaThumbnail

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MangaThumbnailCard(
    mangaThumbnail: MangaThumbnail,
    onItemClicked: (MangaThumbnail) -> Unit
){
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,
        modifier = Modifier.padding(all = 5.dp),
        onClick = {
            onItemClicked(mangaThumbnail)
        }
    ) {
        Box(modifier = Modifier
            .height(270.dp)
            .width(180.dp)){
            val painter = rememberAsyncImagePainter(model = mangaThumbnail.imageURL)
            Image(
                painter = painter, contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 300f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ){
                Text(text = mangaThumbnail.name, style = TextStyle(Color.White, fontSize = 16.sp))
            }
        }
    }
}