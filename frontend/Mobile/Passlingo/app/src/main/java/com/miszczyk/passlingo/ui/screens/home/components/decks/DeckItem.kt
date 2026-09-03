package com.miszczyk.passlingo.ui.screens.home.components.decks

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.borderThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.elevationExtraSmall
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun DeckItem(
    @DrawableRes icon: Int,
    nameDeck: String,
    numberCards: Int,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val textToQuantityCards = if (numberCards > 1) "$numberCards cards" else "$numberCards card"

    val border = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground
    val backgroundIcon = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = elevationExtraSmall,
                shape = RoundedCornerShape(cornerRadiusDefault)
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(cornerRadiusDefault)
            )
            .border(
                borderThin,
                color = border,
                RoundedCornerShape(cornerRadiusDefault)
            )
            .clip(RoundedCornerShape(cornerRadiusDefault))
            .clickable { onClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = backgroundIcon,
                    shape = RoundedCornerShape(cornerRadiusDefault)
                )
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(R.string.content_desc_deck_icon),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(65.dp).padding(5.dp)
            )
        }
        Spacer(modifier = Modifier.width(20.dp))

        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = nameDeck,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontFamily = vagRoundedBold,
            )
            Text(
                text = textToQuantityCards,
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 15.sp,
                fontFamily = vagRoundedBold,
            )
        }
    }
}