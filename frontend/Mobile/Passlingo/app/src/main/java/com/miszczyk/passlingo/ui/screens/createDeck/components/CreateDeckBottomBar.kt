package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun CreateDeckBottomBar(
    deckName: String, addedCards: Int, onSaveDeckClicked: () -> Unit
) {
    val check = deckName.isNotBlank() && addedCards > 0

    val buttonColor by animateColorAsState(
        targetValue = if (check) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
        label = "buttonColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (check) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSecondary,
        label = "buttonColor"
    )

    val textDescription = when {
        (deckName.isBlank() && addedCards > 0) -> stringResource(id = R.string.action_enter_deck_name)
        (deckName.isNotBlank() && addedCards == 0) -> stringResource(id = R.string.action_add_cards_first)
        !check -> stringResource(id = R.string.action_enter_name_add_cards)
        else -> stringResource(id = R.string.action_save_deck)
    }
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
        shape = RoundedCornerShape(size = cornerRadiusDefault),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        onClick = {
            onSaveDeckClicked()
        }) {
        Text(
            text = textDescription,
            fontSize = titleLarge,
            color = textColor,
            fontFamily = vagRoundedBold,
            modifier = Modifier.padding(vertical = spaceDefault)
        )
    }
}
