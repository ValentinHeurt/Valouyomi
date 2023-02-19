package com.example.valouyomi.presentation.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.valouyomi.common.MangaProvider

@Composable
fun MangaProviderItem(
    mangaProvider: MangaProvider,
    onItemClicked: (MangaProvider) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked(mangaProvider) }
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row{
            Image(painter = painterResource(id = mangaProvider.imageID), contentDescription = "Provider",
                modifier = Modifier.size(48.dp))
            Column(modifier = Modifier.padding(start = 24.dp).align(Alignment.CenterVertically)) {
                Text(text = mangaProvider.providerName)
                Text(text = mangaProvider.lang, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(Icons.Default.Star, contentDescription = "Fav", modifier = Modifier.align(Alignment.CenterVertically))
    }
}