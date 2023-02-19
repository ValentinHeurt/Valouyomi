package com.example.valouyomi.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.valouyomi.domain.models.MangaThumbnail

@Composable
fun MangaThumbnailCard(
    mangaThumbnail: MangaThumbnail,
    onItemClicked: (MangaThumbnail) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked(mangaThumbnail) }
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = mangaThumbnail.name)
        Text(text = mangaThumbnail.url)
    }
}