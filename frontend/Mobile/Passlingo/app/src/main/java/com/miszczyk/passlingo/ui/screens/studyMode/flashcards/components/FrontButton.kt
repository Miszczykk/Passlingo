package com.miszczyk.passlingo.ui.screens.studyMode.flashcards.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.borderDash
import com.miszczyk.passlingo.ui.theme.Dimens.borderGap
import com.miszczyk.passlingo.ui.theme.Dimens.borderThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@SuppressLint("LocalContextResourcesRead")
@Composable
fun FrontButton(onClick: () -> Unit) {
    val borderColor = MaterialTheme.colorScheme.onSurface
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge)
            .drawWithCache {
                val strokeWidthPx = borderThin.toPx()
                val dashLengthPx = borderDash.toPx()
                val gapLengthPx = borderGap.toPx()
                onDrawWithContent {
                    drawContent()

                    drawRoundRect(
                        color = borderColor, style = Stroke(
                            width = strokeWidthPx, pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(dashLengthPx, gapLengthPx), phase = 0f
                            )
                        ), cornerRadius = CornerRadius(x = cornerRadiusDefault.toPx())
                    )
                }
            },
        shape = RoundedCornerShape(size = cornerRadiusDefault),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = { onClick() }
    ) {
        val context = LocalContext.current
        val screenWidthPx = context.resources.displayMetrics.widthPixels
        val dynamicFontSize = if (screenWidthPx == 1440) titleMedium else titleLarge
        Text(
            text = stringResource(id = R.string.label_tap_card_to_reveal),
            fontSize = dynamicFontSize,
            color = MaterialTheme.colorScheme.onSecondary,
            fontFamily = vagRoundedBold,
            modifier = Modifier.padding(vertical = spaceDefault)
        )
    }
}