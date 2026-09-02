package com.miszczyk.passlingo.ui.screens.createDeck.model

data class Flashcard(
    val id: String = java.util.UUID.randomUUID().toString(),
    val front: String,
    val back: String,
)
