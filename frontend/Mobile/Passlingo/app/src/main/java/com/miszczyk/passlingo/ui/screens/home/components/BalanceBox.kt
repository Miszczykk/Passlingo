package com.miszczyk.passlingo.ui.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.ShadowCard
import com.miszczyk.passlingo.ui.components.TimeToCard
import com.miszczyk.passlingo.ui.components.TitleToCard
import com.miszczyk.passlingo.ui.screens.home.util.formatTime
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.TextSize.displayMedium
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium

@Composable
fun BalanceBox(modifier: Modifier = Modifier, balanceTime: Long) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TitleToCard(stringResource(R.string.label_available_balance), titleMedium)

        Spacer(modifier = Modifier.height(spaceMedium))

        ShadowCard {
            TimeToCard(
                formatTime(balanceTime),
                displayMedium,
                titleLarge,
                Modifier.offset(y = spaceMedium)
            )
        }
    }
}