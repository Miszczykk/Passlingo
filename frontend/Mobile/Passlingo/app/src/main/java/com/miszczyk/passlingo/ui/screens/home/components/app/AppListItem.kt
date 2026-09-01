package com.miszczyk.passlingo.ui.screens.home.components.app

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.screens.home.model.AppItem
import com.miszczyk.passlingo.ui.screens.home.model.AppRowState
import com.miszczyk.passlingo.ui.util.convertTimeToString
import com.miszczyk.passlingo.ui.util.formatTime
import com.miszczyk.passlingo.ui.theme.Dimens.borderDefault
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.iconLarge
import com.miszczyk.passlingo.ui.theme.Dimens.iconMedium
import com.miszczyk.passlingo.ui.theme.Dimens.iconSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.caption
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun AppListItem(
    modifier: Modifier = Modifier,
    app: AppItem,
    appRowState: AppRowState,
    onClick: () -> Unit
) {
    val checkboxScale by animateFloatAsState(
        targetValue = if (appRowState == AppRowState.Selected) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 300),
        label = "CheckboxScale"
    )

    val rowBackgroundColor by animateColorAsState(
        targetValue = if (appRowState == AppRowState.Locked) {
            MaterialTheme.colorScheme.onBackground
        } else {
            if (appRowState == AppRowState.Selected) MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.15f
            ) else Color.Transparent
        },
        animationSpec = tween(durationMillis = 300),
        label = "RowBackground"
    )

    val circleColor by animateColorAsState(
        targetValue = if (appRowState == AppRowState.Selected) MaterialTheme.colorScheme.secondary else Color.Transparent,
        label = "CircleColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (appRowState == AppRowState.Selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground,
        label = "BorderColor"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadiusDefault))
            .background(rowBackgroundColor)
            .clickable {
                onClick()
            }
            .border(
                width = borderDefault,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(cornerRadiusDefault)
            )
            .padding(spaceLarge), verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            bitmap = app.icon,
            contentDescription = app.name,
            modifier = Modifier.size(iconLarge)
        )

        Spacer(modifier = Modifier.width(spaceDefault))


        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                text = app.name,
                fontFamily = vagRoundedBold,
                fontSize = titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = convertTimeToString(
                    rawTime = formatTime(app.timeInForeground / 1000, false),
                    numberFont = body,
                    textFont = caption
                ),
                fontFamily = vagRoundedBold,
            )
        }

        Box(
            modifier = Modifier
                .size(iconMedium)
                .scale(checkboxScale)
                .border(width = borderDefault, color = borderColor, shape = CircleShape)
                .background(color = circleColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {

            when(appRowState){
                AppRowState.Locked -> {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.content_desc_locked),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(iconSmall)
                    )
                }
                AppRowState.Selected -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.content_desc_checked),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(iconSmall)
                    )
                }
                else -> {}
            }
        }
    }
}