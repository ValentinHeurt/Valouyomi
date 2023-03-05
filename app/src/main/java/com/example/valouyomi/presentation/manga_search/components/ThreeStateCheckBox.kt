package com.example.valouyomi.presentation.manga_search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import com.example.valouyomi.common.CheckBoxState

@Composable
fun ThreeStateCheckbox(
    state: MutableState<CheckBoxState>,
    onStateChanged: (CheckBoxState) -> Unit
){
    var icon by remember {
        when(state.value) {
            CheckBoxState.SELECTED ->  mutableStateOf(Icons.Default.CheckBox)
            CheckBoxState.EXCLUDED ->  mutableStateOf(Icons.Default.IndeterminateCheckBox)
            CheckBoxState.UNSELECTED -> mutableStateOf(Icons.Default.CheckBoxOutlineBlank)
        }
    }
    IconButton(
        onClick = {
            state.value = when(state.value) {
                CheckBoxState.SELECTED -> CheckBoxState.EXCLUDED
                CheckBoxState.EXCLUDED -> CheckBoxState.UNSELECTED
                CheckBoxState.UNSELECTED -> CheckBoxState.SELECTED
            }
            onStateChanged(state.value)
            icon = when(state.value) {
                CheckBoxState.SELECTED   -> Icons.Default.CheckBox
                CheckBoxState.EXCLUDED   -> Icons.Default.IndeterminateCheckBox
                CheckBoxState.UNSELECTED -> Icons.Default.CheckBoxOutlineBlank
            }
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "IconCheckBox"
        )
    }
}