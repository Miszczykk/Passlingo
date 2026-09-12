package com.miszczyk.passlingo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.miszczyk.passlingo.ui.theme.Dimens.borderThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.elevationExtraSmall

@Composable
fun Modifier.cardSurface(cornerRadius: Dp = cornerRadiusDefault, borderColor: Color = MaterialTheme.colorScheme.onBackground, borderWidth: Dp = borderThin, elevation: Dp = elevationExtraSmall, backgroundColor: Color = MaterialTheme.colorScheme.background): Modifier{
    return this
        .shadow(
            elevation = elevation,
            shape = RoundedCornerShape(size = cornerRadius)
        )
        .background(
            color = backgroundColor,
            shape = RoundedCornerShape(size = cornerRadius)
        )
        .border(
            width = borderWidth,
            color = borderColor,
            shape = RoundedCornerShape(size = cornerRadius)
        )
}