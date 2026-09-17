package com.miszczyk.passlingo.ui.screens.loading

import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.buildAppNameLogo
import com.miszczyk.passlingo.ui.theme.PasslingoTheme
import com.miszczyk.passlingo.ui.theme.TextSize.displayHuge
import com.miszczyk.passlingo.ui.theme.TextSize.displayMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, onAnimationFinished: () -> Unit = {}) {
    val offsetY = remember { Animatable(initialValue = 0f) }
    val context = LocalContext.current

    DisposableEffect(key1 = Unit) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.intro_music)
        mediaPlayer.start()

        onDispose {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.release()
        }
    }

    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = 600)

        offsetY.animateTo(
            targetValue = 900f, animationSpec = tween(durationMillis = 980, easing = CubicBezierEasing(
                0.1f,
                0.8f,
                0.3f,
                1.0f
            )
            )
        )

        offsetY.animateTo(
            targetValue = -3000f, animationSpec = tween(durationMillis = 2000, easing = CubicBezierEasing(0.05f, 0.95f, 0.1f, 1.0f))
        )
        onAnimationFinished()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = buildAppNameLogo(smallSize = displayMedium, bigSize = displayHuge),
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