package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun CardsContainer(addedCards: Int){
    if(addedCards > 0){

    }else{
        Text(
            text = "No cards added yet. Add manually!",
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = vagRoundedLight,
            fontSize = 14.sp,
        )
    }
}