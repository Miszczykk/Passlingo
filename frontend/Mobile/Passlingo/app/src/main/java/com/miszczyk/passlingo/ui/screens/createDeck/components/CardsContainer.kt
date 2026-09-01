package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun CardsContainer(addedCards: Int){
    if(addedCards > 0){

    }else{
        Text(
            text = stringResource(R.string.prompt_no_cards_added),
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = vagRoundedLight,
            fontSize = 14.sp,
        )
    }
}