package com.miszczyk.passlingo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miszczyk.passlingo.ui.screens.home.components.BalanceBox
import com.miszczyk.passlingo.ui.screens.home.components.CreateBox
import com.miszczyk.passlingo.ui.screens.home.components.Header
import com.miszczyk.passlingo.ui.screens.home.components.decks.DeckBox
import com.miszczyk.passlingo.ui.screens.home.viewmodel.DeckViewModel
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.PasslingoTheme


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: DeckViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Header()
        Spacer(modifier = Modifier.height(spaceExtraLarge))
        BalanceBox(balanceTime = uiState.balanceTime)
        Spacer(modifier = Modifier.height(spaceHuge))
        CreateBox()
        Spacer(modifier = Modifier.height(spaceHuge))
        DeckBox()
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