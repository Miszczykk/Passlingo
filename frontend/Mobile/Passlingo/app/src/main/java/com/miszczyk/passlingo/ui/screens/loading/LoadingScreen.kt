package com.miszczyk.passlingo.ui.screens.loading

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.PasslingoTheme
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, onAnimationFinished: () -> Unit = {}) {
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(500)

        offsetY.animateTo(
            targetValue = 400f,
            animationSpec = tween(durationMillis = 400, easing = EaseOut)
        )

        offsetY.animateTo(
            targetValue = -3000f,
            animationSpec = tween(durationMillis = 300, easing = EaseIn)
        )
        delay(500)
        onAnimationFinished()
    }

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontSize = 75.sp)){
            append(stringResource(R.string.app_name))
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary, fontSize = 150.sp)){
            append('.')
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(
            text = annotatedText,
            modifier = modifier.fillMaxWidth().graphicsLayer { translationY = offsetY.value },
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