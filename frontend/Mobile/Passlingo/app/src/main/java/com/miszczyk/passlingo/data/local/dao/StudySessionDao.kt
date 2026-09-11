package com.miszczyk.passlingo.data.local.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.miszczyk.passlingo.data.local.entity.StudyCardProgressEntity
import com.miszczyk.passlingo.data.local.entity.StudySessionEntity

data class StudySessionWithProgress(
    @Embedded val session: StudySessionEntity,
    @Relation(parentColumn = "id", entityColumn = "sessionId")
    val cardProgress: List<StudyCardProgressEntity>
)

@Dao
interface StudySessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: List<StudyCardProgressEntity>)

    @Transaction
    suspend fun createSession(session: StudySessionEntity, progress: List<StudyCardProgressEntity>){
        insertSession(session)
        insertProgress(progress)
    }

    @Query("DELETE FROM study_sessions WHERE deckId = :deckId")
    suspend fun deleteSessionByDeckId(deckId: String)

    @Query("DELETE FROM study_sessions WHERE id = :sessionId")
    suspend fun deleteSessionById(sessionId: String)

    @Transaction
    @Query("SELECT * FROM study_sessions WHERE deckId = :deckId LIMIT 1")
    suspend fun getActiveSession(deckId: String): StudySessionWithProgress?


    // SESSION STATUS
    @Query("SELECT EXISTS(SELECT 1 FROM study_sessions WHERE deckId = :deckId)")
    suspend fun doesSessionExist(deckId: String): Boolean

    @Query("SELECT COUNT(*) FROM study_card_progress WHERE sessionId = :sessionId AND currentRound = :targetRounds")
    suspend fun getFinishedCard(sessionId: String, targetRounds: Int): Int

    @Query("SELECT COUNT(*) FROM study_card_progress WHERE sessionId = :sessionId")
    suspend fun getTotalCardCount(sessionId: String): Int


    // UPDATE STATUS
    @Query("UPDATE study_card_progress SET currentRound = 0 WHERE sessionId = :sessionId AND flashcardId = :flashcardId")
    suspend fun resetCurrentRound(sessionId: String, flashcardId: String)

    @Query("UPDATE study_card_progress SET currentRound = currentRound + 1 WHERE sessionId = :sessionId AND flashcardId = :flashcardId")
    suspend fun incrementCurrentRound(sessionId: String, flashcardId: String)

    @Query("UPDATE study_card_progress SET attempts = attempts + 1 WHERE sessionId = :sessionId AND flashcardId = :flashcardId")
    suspend fun incrementAttempts(sessionId: String, flashcardId: String)

    @Query("SELECT * FROM study_card_progress WHERE sessionId = :sessionId AND currentRound < :targetRound ORDER BY currentRound ASC LIMIT 10")
    suspend fun getNextBatch(sessionId: String, targetRound: Int): List<StudyCardProgressEntity>

    @Query("SELECT * FROM study_card_progress WHERE sessionId = :sessionId AND attempts > 0 ORDER BY attempts DESC")
    suspend fun getCardToPractice(sessionId: String): List<StudyCardProgressEntity>
}