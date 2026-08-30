package com.miszczyk.passlingo.ui.screens.createDeck

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.miszczyk.passlingo.ui.screens.createDeck.components.AddCardSection
import com.miszczyk.passlingo.ui.screens.createDeck.components.DeckDetailsSection
import com.miszczyk.passlingo.ui.screens.createDeck.components.Header
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge

@Composable
fun CreateDeckScreen(modifier: Modifier = Modifier, onBack: () -> Unit){
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Header(onClick = onBack)
        Spacer(modifier = Modifier.height(spaceExtraLarge))
        DeckDetailsSection()

        Spacer(modifier = Modifier.height(spaceExtraHuge))

        val lineColor = MaterialTheme.colorScheme.onSecondary
        Canvas(modifier = Modifier.fillMaxWidth().padding(horizontal = spaceExtraLarge)) {
            drawLine(
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = size.width, y = 0f),
                color = lineColor,
                strokeWidth = 1f
            )
        }

        Spacer(modifier = Modifier.height(spaceHuge))
        AddCardSection()
    }
}