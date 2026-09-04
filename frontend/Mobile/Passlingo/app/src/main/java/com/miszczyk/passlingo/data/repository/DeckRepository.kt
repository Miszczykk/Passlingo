package com.miszczyk.passlingo.data.repository

import android.content.Context
import com.miszczyk.passlingo.data.local.PasslingoDatabase
import com.miszczyk.passlingo.data.local.entity.DeckEntity
import com.miszczyk.passlingo.data.local.entity.DeckWithFlashcards
import com.miszczyk.passlingo.data.local.entity.FlashcardEntity
import com.miszczyk.passlingo.ui.screens.createDeck.model.Flashcard
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class DeckRepository(context: Context) {
    private val deckDao = PasslingoDatabase.getInstance(context).deckDao()

    val allDecks: Flow<List<DeckWithFlashcards>> = deckDao.getAllDecksWithFlashcards()

    suspend fun saveDeck(name: String, iconResId: Int, cards: List<Flashcard>){
        val deckId = UUID.randomUUID().toString()

        val deckEntity = DeckEntity(id = deckId, name = name, iconResId = iconResId)
        val cardEntities = cards.map {card ->
            FlashcardEntity(id = card.id, deckId = deckId, front = card.front, back = card.back)
        }
        deckDao.insertDeckWithFlashcards(deckEntity, cardEntities)
    }

    suspend fun deleteDeck(deckId: String?){
        val deck  = deckDao.getDeckById(deckId ?: "") ?: return
        deckDao.deleteDeck(deck)
    }
}