package com.miszczyk.passlingo

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.miszczyk.passlingo.ui.screens.home.HomeScreen
import com.miszczyk.passlingo.ui.screens.loading.LoadingScreen
import com.miszczyk.passlingo.ui.theme.PasslingoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            PasslingoTheme {
                var isLoadingFinished by remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    if (isLoadingFinished) {
                        HomeScreen(modifier = Modifier.padding(innerPadding))
                    } else {
                        LoadingScreen(
                            modifier = Modifier.padding(innerPadding),
                            onAnimationFinished = { isLoadingFinished = true }
                        )
                    }
                }
            }
        }
    }
}

