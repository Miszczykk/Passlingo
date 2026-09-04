package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.borderDefault
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.elevationNone
import com.miszczyk.passlingo.ui.theme.Dimens.elevationSmall
import com.miszczyk.passlingo.ui.theme.Dimens.iconExtraLarge

@Composable
fun IconItem(
    @DrawableRes iconResId: Int, isSelected: Boolean, onClick: () -> Unit
) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
    val borderColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    val iconColor =
        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface

    IconButton(
        onClick = { onClick() }, modifier = Modifier
            .shadow(
                elevation = if (isSelected) elevationSmall else elevationNone,
                shape = RoundedCornerShape(size = cornerRadiusDefault)
            )
            .aspectRatio(ratio = 1f)
            .clip(shape = RoundedCornerShape(size = cornerRadiusDefault))
            .background(backgroundColor)
            .border(
                width = borderDefault,
                color = borderColor,
                shape = RoundedCornerShape(size = cornerRadiusDefault)
            )
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = stringResource(id = R.string.content_desc_deck_icon),
            tint = iconColor,
            modifier = Modifier.size(size = iconExtraLarge)
        )
    }
}