package com.example.valouyomi.presentation.manga_details

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.valouyomi.domain.models.Chapter

@Composable
fun ChapterPreview(
    chapter: Chapter,
    modifier: Modifier
){
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier) {
        Column(modifier = Modifier.width(325.dp)) {
            Text(text = chapter.name, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = chapter.date + " - " + chapter.views, color = Color.White)
        }
        Icon(imageVector = Icons.Default.Download, contentDescription = "Download button", tint = Color.White)
    }
}