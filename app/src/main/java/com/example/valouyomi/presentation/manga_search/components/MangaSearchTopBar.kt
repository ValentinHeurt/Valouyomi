package com.example.valouyomi.presentation.manga_search.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.valouyomi.presentation.manga_search.MangaSearchViewModel
import com.example.valouyomi.presentation.manga_search.util.SearchAppBarState
import com.example.valouyomi.presentation.manga_search.util.TrailingIconState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MangaSearchTopBar(
    viewModel: MangaSearchViewModel = hiltViewModel(),
    sheetState: BottomSheetState,
    providerName: String
){
    if(viewModel.searchAppBarState.value == SearchAppBarState.CLOSED){
        DefaultTopBar(providerName = providerName, sheetState = sheetState)
    }
    else{
        SearchTopBar(
            text = viewModel.searchTextState.value,
            onTextChange = { text -> viewModel.searchTextState.value = text},
            onCloseClicked = {
                viewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                viewModel.searchTextState.value = ""
            },
            onSearchClicked = {}
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DefaultTopBar(
    viewModel: MangaSearchViewModel = hiltViewModel(),
    providerName: String,
    sheetState : BottomSheetState
){
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
            TopAppBar(
                backgroundColor = Color.Gray,
                elevation = 0.dp,
                title = {
                    Text(text = providerName, color = Color.White)
                },
                actions = {
                    SearchAction(onSearchClicked = {viewModel.searchAppBarState.value = SearchAppBarState.OPENED})
                    FilterAction(onFilterClicked = { scope.launch { if (sheetState.isCollapsed) sheetState.expand() else sheetState.collapse() }})
                }
            )
        }
}

@Composable
fun SearchAction(
    viewModel: MangaSearchViewModel = hiltViewModel(),
    onSearchClicked: () -> Unit
){
    val context = LocalContext.current
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "search",
            tint = Color.White
        )
    }
}

@Composable
fun FilterAction(onFilterClicked: () -> Unit){
    val context = LocalContext.current
    IconButton(onClick = onFilterClicked) {
        Icon(
            imageVector = Icons.Filled.FilterList,
            contentDescription = "filter",
            tint = Color.White
        )
    }
}

@Composable
fun SearchTopBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    viewModel: MangaSearchViewModel = hiltViewModel()
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Surface(
            color = Color.Gray,
            elevation = 0.dp
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = {
                    onTextChange(it)
                },
                placeholder = {
                    Text(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        text = "String",
                        color = Color.White
                    )
                },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        onClick = { viewModel.getMangaThumbnails()}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        when (viewModel.trailingIconState.value) {
                            TrailingIconState.DELETE -> {
                                onTextChange("")
                                viewModel.trailingIconState.value = TrailingIconState.CLOSE
                            }
                            TrailingIconState.CLOSE -> {
                                if (text.isNotEmpty()) {
                                    onTextChange("")
                                } else {
                                    onCloseClicked()
                                    viewModel.trailingIconState.value = TrailingIconState.DELETE
                                }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClicked()
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent
                )
            )
        }
    }
}