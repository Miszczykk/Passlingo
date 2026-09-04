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
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.borderThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.elevationExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.iconExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun DeckItem(
    @DrawableRes icon: Int,
    nameDeck: String,
    flashcardCount: Int,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val borderColor =
        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground
    val iconBackgroundColor =
        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = elevationExtraSmall,
                shape = RoundedCornerShape(size = cornerRadiusDefault)
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(size = cornerRadiusDefault)
            )
            .border(
                width = borderThin,
                color = borderColor,
                shape = RoundedCornerShape(size = cornerRadiusDefault)
            )
            .clip(shape = RoundedCornerShape(size = cornerRadiusDefault))
            .clickable { onClick() }
            .padding(all = spaceLarge), verticalAlignment = Alignment.CenterVertically) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier.background(
                color = iconBackgroundColor, shape = RoundedCornerShape(size = cornerRadiusDefault)
            )
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.content_desc_deck_icon),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(size = iconExtraHuge)
                    .padding(all = spaceExtraSmall)
            )
        }
        Spacer(modifier = Modifier.width(width = spaceLarge))

        Column(
            modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = nameDeck,
                color = MaterialTheme.colorScheme.primary,
                fontSize = titleMedium,
                fontFamily = vagRoundedBold,
            )
            Text(
                text = "$flashcardCount card(s)",
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = body,
                fontFamily = vagRoundedBold,
            )
        }
    }
}