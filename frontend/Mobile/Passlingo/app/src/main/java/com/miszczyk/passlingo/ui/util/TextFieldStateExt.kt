package com.miszczyk.passlingo.ui.util

import androidx.compose.foundation.text.input.TextFieldState

fun TextFieldState.clear(){
    edit {
        replace(start = 0, end = length, text = "")
    }
}

fun TextFieldState.setText(newText: String){
    edit {
        replace(start = 0, end = length, text = newText)
    }
}