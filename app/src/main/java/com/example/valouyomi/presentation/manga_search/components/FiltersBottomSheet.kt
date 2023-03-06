package com.example.valouyomi.presentation.manga_search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.valouyomi.presentation.components.DropDown
import com.example.valouyomi.presentation.manga_search.MangaSearchViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FiltersBottomSheet(
    viewModel: MangaSearchViewModel = hiltViewModel()
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
        .height(500.dp)
        .background(Color.Gray)
    ){
        val state = rememberScrollState()
        Column(modifier =  Modifier.verticalScroll(state)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Reset")
                }
                Button(onClick = { viewModel.getMangaThumbnails() }, modifier = Modifier.background(Color.Blue)) {
                    Text(text = "Filter")
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Sort")
                var isExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = {isExpanded = !isExpanded},
                    modifier = Modifier.width(150.dp)
                ) {
                    TextField(
                        value = viewModel.selectedSortProtocol.value,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)},
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        viewModel.sortList.value.keys.forEach{ option ->
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.selectedSortProtocol.value = option
                                    isExpanded = false
                                }
                            ) {
                                Text(text = option)
                            }
                        }
                    }
                }
            }
            
            DropDown(
                text = "Genres",
                modifier = Modifier.padding(15.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ){
                        viewModel.genresState.value.genres.toList().forEach(){
                            val genre = it.first
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ThreeStateCheckbox(state = it.second, onStateChanged = { viewModel.genresState.value.genres[genre]?.value = it})
                                Text(text = it.first, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}