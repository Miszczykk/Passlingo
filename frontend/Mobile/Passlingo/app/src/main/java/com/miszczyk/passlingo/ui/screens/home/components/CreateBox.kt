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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBox() {
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isAnimating) 0.2f else 0.15f,
        animationSpec = tween(durationMillis = 300),
        label = "AlphaAnimation"
    )

    val animatedRadiusMultiplier by animateFloatAsState(
        targetValue = if (isAnimating) 1.5f else 1.0f,
        animationSpec = tween(durationMillis = 300),
        label = "RadiusAnimation"
    )

    val animatedRotation by animateFloatAsState(
        targetValue = if (isAnimating) 45f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "RotationAnimation"
    )

    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        Button(
            onClick = {
                coroutineScope.launch {
                    isAnimating = true
                    delay(300)
                    isAnimating = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 6.dp
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                val circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = animatedAlpha)
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        color = circleColor,
                        radius = 250f * animatedRadiusMultiplier,
                        center = Offset(x = size.width, y = 0f)
                    )
                    drawCircle(
                        color = circleColor,
                        radius = 200f * animatedRadiusMultiplier,
                        center = Offset(x = 0f, y = size.height)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            )
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create flashcards",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(40.dp)
                                .rotate(animatedRotation)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Create flashcards",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 30.sp,
                        fontFamily = vagRoundedBold,
                    )
                }
            }
        }
    }
}