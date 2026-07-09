package com.miszczyk.passlingo.ui.screens.home.components.decks

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.miszczyk.passlingo.ui.screens.home.model.AppItem
import com.miszczyk.passlingo.ui.screens.home.util.convertTimeToString
import com.miszczyk.passlingo.ui.screens.home.util.formatDuration
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun AppListItem(app: AppItem, isChecked: Boolean, onClick: () -> Unit) {
    val checkboxScale by animateFloatAsState(
        targetValue = if (isChecked) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 300),
        label = "CheckboxScale"
    )

    val rowBackgroundColor by animateColorAsState(
        targetValue = if (isChecked) MaterialTheme.colorScheme.secondary.copy(
            alpha = 0.15f
        ) else Color.Transparent,
        animationSpec = tween(durationMillis = 300),
        label = "RowBackground"
    )

    val circleColor by animateColorAsState(
        targetValue = if (isChecked) MaterialTheme.colorScheme.secondary else Color.Transparent,
        label = "CircleColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isChecked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground,
        label = "BorderColor"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(rowBackgroundColor)
            .clickable {
                onClick()
            }
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        val bitmap = remember(app.icon) { app.icon.toBitmap().asImageBitmap() }

        Image(
            bitmap = bitmap,
            contentDescription = app.name,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))


        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                text = app.name,
                fontFamily = vagRoundedBold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = convertTimeToString(
                    rawTime = formatDuration(app.timeInForeground / 1000),
                    numberFont = 15.sp,
                    textFont = 12.sp
                ),
                fontFamily = vagRoundedBold,
            )

        }
        Box(
            modifier = Modifier
                .size(24.dp)
                .scale(checkboxScale)
                .border(width = 2.dp, color = borderColor, shape = CircleShape)
                .background(color = circleColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Checked",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}