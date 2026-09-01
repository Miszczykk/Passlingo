package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMediumLarge
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun AddCardSection(modifier: Modifier = Modifier, addedCards: Int, stateFront: TextFieldState, stateBack: TextFieldState , onAddToDeckClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.label_deck_cards),
                color = MaterialTheme.colorScheme.primary,
                fontSize = titleLarge,
                fontFamily = vagRoundedBold,
            )
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.label_cards_added, addedCards),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = body,
                    fontFamily = vagRoundedBold,
                    modifier = Modifier.padding(
                        vertical = spaceExtraSmall,
                        horizontal = spaceMediumLarge
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(spaceLarge))


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 0.5.dp, shape = RoundedCornerShape(cornerRadiusDefault))
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(cornerRadiusDefault)
                )
                .border(1.dp, color = MaterialTheme.colorScheme.onBackground, RoundedCornerShape(cornerRadiusDefault)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CardSideInput(Modifier.padding(spaceExtraLarge),"FRONT (WORD)", "Zrozumieć", stateFront)
            CardSideInput(Modifier.padding(horizontal = spaceExtraLarge),"BACK (MEANING)", "To understand", stateBack)
            //TODO ADD IMAGE

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spaceExtraLarge),
                shape = RoundedCornerShape(cornerRadiusDefault),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = {
                    onAddToDeckClicked()
                }) {
                Text(
                    text = "Add to deck",
                    fontSize = 23.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = vagRoundedBold,
                    modifier = Modifier.padding(vertical = spaceDefault)
                )
            }
        }
    }

}