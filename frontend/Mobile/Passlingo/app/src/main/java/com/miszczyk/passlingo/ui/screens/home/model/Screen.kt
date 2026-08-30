package com.miszczyk.passlingo.ui.screens.home.model

sealed interface Screen {
    data object Loading : Screen
    data object Home : Screen
    data object CreateDeck : Screen
}