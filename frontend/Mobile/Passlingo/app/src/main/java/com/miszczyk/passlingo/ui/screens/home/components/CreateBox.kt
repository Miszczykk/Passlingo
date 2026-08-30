package com.miszczyk.passlingo.ui.screens.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.animatedRadiusLarge
import com.miszczyk.passlingo.ui.theme.Dimens.animatedRadiusSmall
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.elevationMedium
import com.miszczyk.passlingo.ui.theme.Dimens.iconLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceNone
import com.miszczyk.passlingo.ui.theme.Dimens.spaceSmall
import com.miszczyk.passlingo.ui.theme.TextSize.headline
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBox(modifier: Modifier = Modifier, onClick: () -> Unit) {
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isAnimating) 0.2f else 0.15f,
        animationSpec = tween(durationMillis = 150),
        label = "AlphaAnimation"
    )

    val animatedRadiusMultiplier by animateFloatAsState(
        targetValue = if (isAnimating) 1.5f else 1.0f,
        animationSpec = tween(durationMillis = 150),
        label = "RadiusAnimation"
    )

    val animatedRotation by animateFloatAsState(
        targetValue = if (isAnimating) 45f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "RotationAnimation"
    )

    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        Button(
            onClick = {
                coroutineScope.launch {
                    isAnimating = true
                    delay(150)
                    isAnimating = false
                    onClick()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spaceExtraLarge),
            shape = RoundedCornerShape(cornerRadiusDefault),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = elevationMedium,
                pressedElevation = elevationMedium
            ),
            contentPadding = PaddingValues(spaceNone)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                val circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = animatedAlpha)
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        color = circleColor,
                        radius = animatedRadiusLarge.toPx() * animatedRadiusMultiplier,
                        center = Offset(x = size.width, y = 0f)
                    )
                    drawCircle(
                        color = circleColor,
                        radius = animatedRadiusSmall.toPx() * animatedRadiusMultiplier,
                        center = Offset(x = 0f, y = size.height)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spaceExtraLarge)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            )
                            .padding(spaceSmall)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.action_create_flashcards),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(iconLarge)
                                .rotate(animatedRotation)
                        )
                    }
                    Spacer(modifier = Modifier.height(spaceLarge))
                    Text(
                        text = stringResource(R.string.action_create_flashcards),
                        color = MaterialTheme.colorScheme.background,
                        fontSize = headline,
                        fontFamily = vagRoundedBold,
                    )
                }
            }
        }
    }
}