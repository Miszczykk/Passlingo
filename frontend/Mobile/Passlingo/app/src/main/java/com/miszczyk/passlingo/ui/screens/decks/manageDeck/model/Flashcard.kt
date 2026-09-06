package com.miszczyk.passlingo.ui.screens.decks.manageDeck.model

import java.util.UUID

data class Flashcard(
    val id: String = UUID.randomUUID().toString(),
    val front: String,
    val back: String,
)
