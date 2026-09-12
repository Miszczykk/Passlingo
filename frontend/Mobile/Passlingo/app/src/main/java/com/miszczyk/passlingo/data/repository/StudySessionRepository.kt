package com.miszczyk.passlingo.data.repository

import android.content.Context
import com.miszczyk.passlingo.data.local.PasslingoDatabase
import com.miszczyk.passlingo.data.local.dao.StudySessionWithProgress
import com.miszczyk.passlingo.data.local.entity.StudyCardProgressEntity
import com.miszczyk.passlingo.data.local.entity.StudyMode
import com.miszczyk.passlingo.data.local.entity.StudySessionEntity

interface StudySessionRepository {
    suspend fun createSession(session: StudySessionEntity, progress: List<StudyCardProgressEntity>)
    suspend fun deleteSessionByDeckId(deckId: String)
    suspend fun deleteSessionById(sessionId: String)
    suspend fun getActiveSession(deckId: String, mode: StudyMode): StudySessionWithProgress?
    suspend fun doesSessionExist(deckId: String, mode: StudyMode): Boolean
    suspend fun getFinishedCard(sessionId: String, targetRounds: Int): Int
    suspend fun getTotalCardCount(sessionId: String): Int
    suspend fun resetCurrentRound(sessionId: String, flashcardId: String)
    suspend fun incrementCurrentRound(sessionId: String, flashcardId: String)
    suspend fun incrementAttempts(sessionId: String, flashcardId: String)
    suspend fun getNextBatch(sessionId: String, targetRound: Int): List<StudyCardProgressEntity>
    suspend fun getCardToPractice(sessionId: String): List<StudyCardProgressEntity>
}

class StudySessionRepositoryImpl(context: Context): StudySessionRepository {
    private val dao = PasslingoDatabase.getInstance(context).studySessionDao()

    override suspend fun createSession(session: StudySessionEntity, progress: List<StudyCardProgressEntity>){
        dao.createSession(session, progress)
    }

    override suspend fun deleteSessionByDeckId(deckId: String) {
        dao.deleteSessionByDeckId(deckId)
    }

    override suspend fun deleteSessionById(sessionId: String) {
        dao.deleteSessionById(sessionId)
    }

    override suspend fun getActiveSession(deckId: String, mode: StudyMode): StudySessionWithProgress? {
        return dao.getActiveSession(deckId, mode)
    }

    override suspend fun doesSessionExist(deckId: String, mode: StudyMode): Boolean {
        return dao.doesSessionExist(deckId, mode)
    }

    override suspend fun getFinishedCard(sessionId: String, targetRounds: Int): Int {
        return dao.getFinishedCard(sessionId, targetRounds)
    }

    override suspend fun getTotalCardCount(sessionId: String): Int {
        return dao.getTotalCardCount(sessionId)
    }

    override suspend fun resetCurrentRound(sessionId: String, flashcardId: String) {
        dao.resetCurrentRound(sessionId, flashcardId)
    }

    override suspend fun incrementCurrentRound(sessionId: String, flashcardId: String) {
        dao.incrementCurrentRound(sessionId, flashcardId)
    }

    override suspend fun incrementAttempts(sessionId: String, flashcardId: String) {
        dao.incrementAttempts(sessionId, flashcardId)
    }

    override suspend fun getNextBatch(sessionId: String, targetRound: Int): List<StudyCardProgressEntity> {
        return dao.getNextBatch(sessionId, targetRound)
    }

    override suspend fun getCardToPractice(sessionId: String): List<StudyCardProgressEntity> {
        return dao.getCardToPractice(sessionId)
    }
}