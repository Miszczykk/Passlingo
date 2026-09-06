package com.miszczyk.passlingo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.miszczyk.passlingo.ui.screens.decks.createDeck.CreateDeckScreen
import com.miszczyk.passlingo.ui.screens.home.HomeScreen
import com.miszczyk.passlingo.ui.model.Screen
import com.miszczyk.passlingo.ui.screens.loading.LoadingScreen
import com.miszczyk.passlingo.ui.theme.PasslingoTheme
import androidx.activity.enableEdgeToEdge
import com.miszczyk.passlingo.ui.screens.decks.editDeck.EditDeckScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasslingoTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Loading) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    when(currentScreen){
                        is Screen.Loading -> LoadingScreen(modifier = Modifier.padding(innerPadding), onAnimationFinished = { currentScreen = Screen.Home })
                        is Screen.Home ->  HomeScreen(modifier = Modifier.padding(innerPadding), onCreateDeckClicked = { currentScreen = Screen.CreateDeck }, onEditDeckClicked = {id -> currentScreen = Screen.EditDeck(deckId = id)})
                        is Screen.CreateDeck -> CreateDeckScreen(modifier = Modifier.padding(innerPadding), onBack = { currentScreen = Screen.Home })
                        is Screen.EditDeck -> EditDeckScreen(
                            modifier = Modifier.padding(innerPadding),
                            deckId = (currentScreen as Screen.EditDeck).deckId,
                            onBack = { currentScreen = Screen.Home }
                        )
                    }
                }
            }
        }
    }
}