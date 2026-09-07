package com.miszczyk.passlingo.ui.util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun rememberSheetCloseHandler(
    sheetState: SheetState,
    onDismissRequest: () -> Unit
): () -> Unit {
    val coroutineScope = rememberCoroutineScope()

    return remember(sheetState, onDismissRequest){
        {
            coroutineScope.launch {
                sheetState.hide()
                onDismissRequest()
            }
        }
    }
}