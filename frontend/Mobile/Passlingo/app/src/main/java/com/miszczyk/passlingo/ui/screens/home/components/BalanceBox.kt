package com.miszczyk.passlingo.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.ui.screens.home.convertTimeToString
import com.miszczyk.passlingo.ui.theme.vagRoundedBlack
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun BalanceBox(modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "AVAILABLE BALANCE",
            textAlign = TextAlign.Center,
            fontFamily = vagRoundedLight,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.5.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .shadow(elevation = 3.dp, shape = RoundedCornerShape(16.dp))
                .background(
                    color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 20.dp)
        ) {
            TimerText()
        }
    }
}

@Composable
private fun TimerText() {
    val rawTime = "01h 45m 30s"

    Text(
        text = convertTimeToString(rawTime, 80.sp, 30.sp),
        textAlign = TextAlign.Center,
        fontFamily = vagRoundedBlack,
        modifier = Modifier.offset(y = (10).dp)
    )
}