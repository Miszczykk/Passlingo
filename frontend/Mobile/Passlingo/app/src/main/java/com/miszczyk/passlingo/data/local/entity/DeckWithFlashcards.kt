package com.miszczyk.passlingo.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DeckWithFlashcards(
    @Embedded val deck: DeckEntity,
    @Relation(parentColumn = "id", entityColumn = "deckId")
    val flashcards: List<FlashcardEntity>
)