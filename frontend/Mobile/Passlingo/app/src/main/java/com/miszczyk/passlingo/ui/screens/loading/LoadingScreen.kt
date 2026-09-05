package com.miszczyk.passlingo.ui.screens.loading

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.miszczyk.passlingo.ui.components.AppNameLogo
import com.miszczyk.passlingo.ui.theme.PasslingoTheme
import com.miszczyk.passlingo.ui.theme.TextSize.displayHuge
import com.miszczyk.passlingo.ui.theme.TextSize.displayMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, onAnimationFinished: () -> Unit = {}) {
    val offsetY = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = 500)

        offsetY.animateTo(
            targetValue = 400f, animationSpec = tween(durationMillis = 400, easing = EaseOut)
        )

        offsetY.animateTo(
            targetValue = -3000f, animationSpec = tween(durationMillis = 300, easing = EaseIn)
        )
        delay(timeMillis = 500)
        onAnimationFinished()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = AppNameLogo(displayMedium, displayHuge),
            modifier = modifier
                .fillMaxWidth()
                .graphicsLayer { translationY = offsetY.value },
            textAlign = TextAlign.Center,
            fontFamily = vagRoundedBold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    PasslingoTheme {
        LoadingScreen()
    }
}