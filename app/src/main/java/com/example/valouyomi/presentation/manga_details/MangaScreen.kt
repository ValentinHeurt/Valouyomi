package com.example.valouyomi.presentation.manga_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.valouyomi.presentation.Screen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MangaScreen(
    navController: NavController,
    viewModel: MangaViewModel = hiltViewModel()
){
    val manga = viewModel.manga.value
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                //Image manga zoomé
                val painter = rememberAsyncImagePainter(model = manga.imageURL)
                Image(
                    painter = painter, contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                //Gradiant transparent vers couleur
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.DarkGray.copy(alpha = 0.55f),
                                    Color.DarkGray
                                )
                            )
                        )
                        .fillMaxSize()
                )
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        //une image manga
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            elevation = 5.dp,
                            modifier = Modifier
                                .padding(start = 30.dp)
                                .height(160.dp)
                                .width(120.dp)
                        ){
                            val painter = rememberAsyncImagePainter(model = manga.imageURL)
                            Image(
                                painter = painter, contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(modifier = Modifier.padding(top = 30.dp, end = 30.dp)){
                            Text(text = manga.title, color = Color.White, fontSize = 20.sp)
                            //Auteurs
                            Text(text = if(manga.authors.count() > 0) manga.authors[0] else "", color = Color.White)
                            //Status
                            Text(text = manga.status, color = Color.White)
                        }
                    }
                    Row(modifier = Modifier.padding(top = 40.dp)) {
                        Column() {
                            //Icon ajouter biblio
                            Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Biblio", tint = Color.White)
                            //Text dans la biblio
                            //Text(text = "Ajouter dans la bibliothèque NYI", color = Color.White)
                        }
/*                        Column() {
                            //Icon Web
                            //Text Webview
                        }*/
                    }
                }
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)) {
                //Description
                var expand by remember { mutableStateOf(false) }
                ExpandableText(
                    originalText = manga.description,
                    expandAction = "See more",
                    modifier = Modifier
                        .clickable { expand = !expand }
                        .background(Color.DarkGray)
                        .padding(start = 5.dp, end = 5.dp),
                    isExpanded = expand,
                    expandActionColor = Color.Gray,
                    color = Color.White
                )
                //Text(text = manga.description, color = Color.White, modifier = Modifier.padding(start = 5.dp, end = 5.dp), maxLines = 4)
            }
            LazyColumn(modifier = Modifier.fillMaxSize().background(color = Color.DarkGray).padding(top = 20.dp)){
                items(manga.chapters){chapter ->
                    ChapterPreview(chapter = chapter, modifier = Modifier.fillMaxWidth().padding(10.dp).clickable(onClick = {
                        val encodedUrl = URLEncoder.encode(chapter.chapterUrl, StandardCharsets.UTF_8.toString())
                        navController.navigate(Screen.MangaReaderScreen.route + "/${encodedUrl}/${viewModel.providerParam}")
                    }))
                }
            }
        }
    }
}