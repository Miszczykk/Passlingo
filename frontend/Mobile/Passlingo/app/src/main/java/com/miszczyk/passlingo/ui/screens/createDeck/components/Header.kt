package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMediumLarge
import com.miszczyk.passlingo.ui.theme.TextSize.headline
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun Header(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge)
    ) {
        IconButton(
            onClick = { onClick() },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(spaceMediumLarge)
                .background(
                    MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(cornerRadiusDefault)
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.content_desc_lock),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Create Deck",
            color = MaterialTheme.colorScheme.primary,
            fontSize = headline,
            fontFamily = vagRoundedBold,
        )
    }
}