package com.miszczyk.passlingo.ui.screens.home.components.decks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.borderThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.elevationExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.iconHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun WithoutDecks() {
    Column(
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
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(size = cornerRadiusDefault)
            )
            .padding(all = spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier.background(
                color = MaterialTheme.colorScheme.onBackground, shape = CircleShape
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.write),
                contentDescription = stringResource(id = R.string.action_create_flashcards),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(size = iconHuge)
            )
        }
        Spacer(modifier = Modifier.height(height = spaceLarge))
        Text(
            text = stringResource(id = R.string.prompt_no_decks_yet),
            color = MaterialTheme.colorScheme.primary,
            fontSize = titleMedium,
            fontFamily = vagRoundedBold,
        )
        Spacer(modifier = Modifier.height(height = spaceExtraSmall))
        Text(
            text = stringResource(id = R.string.prompt_create_first_deck),
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = body,
            fontFamily = vagRoundedLight,
        )
    }
}