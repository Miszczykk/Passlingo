package com.miszczyk.passlingo.ui.screens.studyMode.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.data.local.entity.DeckWithFlashcards
import com.miszczyk.passlingo.data.local.entity.StudyCardProgressEntity
import com.miszczyk.passlingo.data.local.entity.StudyMode
import com.miszczyk.passlingo.data.local.entity.StudySessionEntity
import com.miszczyk.passlingo.data.repository.DeckRepository
import com.miszczyk.passlingo.data.repository.StudySessionRepositoryImpl
import com.miszczyk.passlingo.ui.screens.home.data.TimeAndAppsRepository
import com.miszczyk.passlingo.ui.screens.studyMode.model.PracticeCardUiModel
import kotlinx.coroutines.launch
import java.util.UUID

abstract class BaseStudyViewModel(application: Application, private val studyMode: StudyMode) :
    AndroidViewModel(application) {
    protected val sessionRepository = StudySessionRepositoryImpl(context = application)
    protected val timeRepository = TimeAndAppsRepository(context = application)
    protected val deckRepository = DeckRepository(context = application)

    protected var currentDeckId: String = ""
    protected var currentSessionId: String = ""
    protected var targetRounds: Int = 1
    protected var currentBatch: List<StudyCardProgressEntity> = emptyList()
    protected var deckDictionary: Map<String, Pair<String, String>> = emptyMap()
    protected var timeToBreath = 0
    protected var totalCardsInSession: Int = 0

    protected abstract val logTag: String

    protected abstract fun onResetUiState()
    protected abstract fun onHideBreatherState()
    protected abstract fun onShowErrorState(message: String)
    protected abstract fun onSessionEndedUiState()
    protected abstract fun onPracticeCardsLoaded(cards: List<PracticeCardUiModel>)

    protected abstract suspend fun showCurrentCard()
    protected open suspend fun onSessionInitialized() {}

    fun startSession(deckId: String, rounds: Int) {
        currentDeckId = deckId
        timeToBreath = 0

        viewModelScope.launch {
            onResetUiState()

            runCatching {
                val deckWithCards = deckRepository.getDeckWithFlashcardsById(deckId = deckId)

                deckDictionary = deckWithCards?.flashcards?.associate {
                    it.id to Pair(first = it.front, second = it.back)
                } ?: emptyMap()

                val activeSession =
                    sessionRepository.getActiveSession(deckId = deckId, mode = studyMode)

                if (activeSession != null) {
                    currentSessionId = activeSession.session.id
                    targetRounds = activeSession.session.targetRounds
                } else {
                    targetRounds = rounds
                    createNewSession(deckId, deckWithCards)
                }

                totalCardsInSession = sessionRepository.getTotalCardCount(currentSessionId)
                onSessionInitialized()
                loadNextBatchAndShow()
            }.onFailure { error ->
                Log.e(logTag, "Failed to start session", error)
                onShowErrorState(getApplication<Application>().getString(R.string.error_study_session))
            }
        }
    }

    private suspend fun createNewSession(deckId: String, deckWithCards: DeckWithFlashcards?) {
        currentSessionId = UUID.randomUUID().toString()
        val session = StudySessionEntity(
            id = currentSessionId, deckId = deckId, mode = studyMode, targetRounds = targetRounds
        )

        val progressList = deckWithCards?.flashcards?.shuffled()?.mapIndexed { index, card ->
            StudyCardProgressEntity(
                id = UUID.randomUUID().toString(),
                sessionId = currentSessionId,
                flashcardId = card.id,
                currentRound = 0,
                attempts = 0,
                orderIndex = index
            )
        } ?: emptyList()
        sessionRepository.createSession(session, progressList)
    }

    fun continueLearningClicked() {
        viewModelScope.launch {
            runCatching {
                timeToBreath = 0
                onHideBreatherState()
                loadNextBatchAndShow()
            }.onFailure { error ->
                Log.e(logTag, "Failed to continue learning", error)
                onShowErrorState(getApplication<Application>().getString(R.string.error_study_session))
            }
        }
    }

    protected suspend fun loadNextBatchAndShow() {
        currentBatch = sessionRepository.getNextBatch(currentSessionId, targetRounds)
        if (currentBatch.isEmpty()) {
            loadCardsToPractice()

            if (currentSessionId.isNotEmpty()) {
                sessionRepository.deleteSessionById(currentSessionId)
            }
            onSessionEndedUiState()
        } else {
            showCurrentCard()
        }
    }

    private suspend fun loadCardsToPractice() {
        val progressCards = sessionRepository.getCardToPractice(currentSessionId)
        val mappedCards = progressCards.map { progress ->
            val texts = deckDictionary[progress.flashcardId]
            PracticeCardUiModel(
                id = progress.id,
                front = texts?.first ?: "Unknown",
                back = texts?.second ?: "Unknown",
                attempts = progress.attempts
            )
        }
        onPracticeCardsLoaded(mappedCards)
    }
}