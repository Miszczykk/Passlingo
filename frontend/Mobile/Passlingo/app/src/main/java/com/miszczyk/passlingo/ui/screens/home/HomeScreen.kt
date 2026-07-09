package com.miszczyk.passlingo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miszczyk.passlingo.ui.screens.home.components.BalanceBox
import com.miszczyk.passlingo.ui.screens.home.components.CreateBox
import com.miszczyk.passlingo.ui.screens.home.components.Header
import com.miszczyk.passlingo.ui.screens.home.components.decks.DecksBox
import com.miszczyk.passlingo.ui.theme.PasslingoTheme


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {


    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Header()
        Spacer(modifier = Modifier.height(30.dp))
        BalanceBox()
        Spacer(modifier = Modifier.height(40.dp))
        CreateBox()
        Spacer(modifier = Modifier.height(40.dp))
        DecksBox()
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    PasslingoTheme {
        HomeScreen()
    }
}