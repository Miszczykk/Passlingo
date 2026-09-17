package com.miszczyk.passlingo.ui.screens.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.CardTime
import com.miszczyk.passlingo.ui.components.CardTitle
import com.miszczyk.passlingo.ui.components.ShadowCard
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.TextSize.displayMedium
import com.miszczyk.passlingo.ui.theme.TextSize.displaySmallLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.util.formatTime

@SuppressLint("LocalContextResourcesRead")
@Composable
fun BalanceBox(
    balanceTime: Long
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CardTitle(
            titleText = stringResource(id = R.string.label_available_balance),
            titleFontSize = titleMedium
        )

        Spacer(modifier = Modifier.height(height = spaceMedium))

        ShadowCard {
            val context = LocalContext.current
            val screenWidthPx = context.resources.displayMetrics.widthPixels
            val dynamicFontSize = if (screenWidthPx == 1440) displaySmallLarge else displayMedium
            CardTime(
                timeText = formatTime(totalSeconds = balanceTime),
                numberFontSize = dynamicFontSize,
                textFontSize = titleLarge,
                Modifier.offset(y = spaceMedium)
            )
        }
    }
}