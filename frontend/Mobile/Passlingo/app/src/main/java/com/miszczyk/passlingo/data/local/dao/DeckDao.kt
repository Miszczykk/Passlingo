package com.miszczyk.passlingo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.miszczyk.passlingo.data.local.entity.DeckEntity
import com.miszczyk.passlingo.data.local.entity.DeckWithFlashcards
import com.miszczyk.passlingo.data.local.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert
    suspend fun insertDeck(deck: DeckEntity)

    @Insert
    suspend fun insertFlashcards(cards: List<FlashcardEntity>)

    @Transaction
    suspend fun insertDeckWithFlashcards(deck: DeckEntity, cards: List<FlashcardEntity>) {
        insertDeck(deck)
        insertFlashcards(cards)
    }

    @Transaction
    @Query("SELECT * FROM decks ORDER BY createdAt DESC")
    fun getAllDecksWithFlashcards(): Flow<List<DeckWithFlashcards>>

    @Query("SELECT * FROM decks WHERE id = :deckId")
    suspend fun getDeckById(deckId: String): DeckEntity?

    @Delete
    suspend fun deleteDeck(deck: DeckEntity)

    @Transaction
    @Query("SELECT * FROM decks WHERE id = :deckId")
    suspend fun getDeckWithFlashcardsById(deckId: String): DeckWithFlashcards?

    @androidx.room.Update
    suspend fun updateDeck(deck: DeckEntity)

    @Query("DELETE FROM flashcards WHERE deckId = :deckId")
    suspend fun deleteFlashcardsByDeckId(deckId: String)

    @Transaction
    suspend fun updateDeckWithFlashcards(deck: DeckEntity, cards: List<FlashcardEntity>){
        updateDeck(deck)
        deleteFlashcardsByDeckId(deck.id)
        insertFlashcards(cards)
    }
}