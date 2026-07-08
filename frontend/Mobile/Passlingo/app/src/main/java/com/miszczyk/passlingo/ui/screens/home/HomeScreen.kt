package com.miszczyk.passlingo.ui.screens.home

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.miszczyk.passlingo.ui.screens.home.components.BalanceBox
import com.miszczyk.passlingo.ui.screens.home.components.CreateBox
import com.miszczyk.passlingo.ui.screens.home.components.Header
import com.miszczyk.passlingo.ui.screens.home.components.decks.DecksBox
import com.miszczyk.passlingo.ui.theme.PasslingoTheme

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "passlingo_settings")
val BLOCKED_APPS_KEY = stringSetPreferencesKey("blocked_apps")

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
@Composable
fun convertTimeToString(
    rawTime: String,
    numberFont: TextUnit,
    textFont: TextUnit
): AnnotatedString {
    val annotatedTime = buildAnnotatedString {
        rawTime.forEach { char ->
            when {
                char.isDigit() -> {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary, fontSize = numberFont
                        )
                    ) { append(char.toString()) }
                }

                char.isLetter() -> {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.secondary, fontSize = textFont
                        )
                    ) { append(char.toString()) }
                }

                else -> {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Transparent, fontSize = numberFont
                        )
                    ) { append(char.toString()) }
                }
            }
        }
    }
    return annotatedTime
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    PasslingoTheme {
        HomeScreen()
    }
}