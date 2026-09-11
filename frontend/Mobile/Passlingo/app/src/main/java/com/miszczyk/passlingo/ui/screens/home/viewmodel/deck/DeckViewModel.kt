package com.miszczyk.passlingo.ui.screens.home.viewmodel.deck

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.data.repository.DeckRepository
import com.miszczyk.passlingo.data.repository.StudySessionRepositoryImpl
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckBottomSheetState
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckDialogState
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckUiState
import com.miszczyk.passlingo.ui.util.observeWithRetry
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeckViewModel(application: Application) : AndroidViewModel(application) {
    private val deckRepository = DeckRepository(context = application)
    private val studySessionRepository = StudySessionRepositoryImpl(context = application)
    private val _deckUiState = MutableStateFlow(value = DeckUiState())
    val deckUiState: StateFlow<DeckUiState> = _deckUiState.asStateFlow()

    private val deckDialogAction = DeckDialogAction(uiStateFlow = _deckUiState, externalScope = viewModelScope, deckRepository = deckRepository, sessionRepository = studySessionRepository)
    private val deckSelectionAction = DeckSelectionAction(uiStateFlow = _deckUiState)

    private var observationJob: Job? = null

    init {
        startObservingData()
    }
    private fun startObservingData() {
        observationJob?.cancel()
        observationJob = deckRepository.allDecks.observeWithRetry(
            scope = viewModelScope,
            onError = { e->
                _deckUiState.update { it.copy(deckDialogState = DeckDialogState.Error(e.localizedMessage ?: "Failed to load decks")) }
            },
            onEachAction = { decksList ->
                _deckUiState.update { it.copy(decks = decksList) }
            }
        )
    }

    fun onDialogCancelled() = deckDialogAction.onDialogCancelled()
    fun onDialogConfirmed() = deckDialogAction.onDialogConfirmed()

    fun onContinueSessionCancelled() = deckDialogAction.onContinueSessionCancelled()

    fun deleteDeck() = deckSelectionAction.onDeleteDeckClicked()

    fun hideBottomSheet() {
        _deckUiState.update { currentState ->
            currentState.copy(
                deckBottomSheetState = DeckBottomSheetState.None,
                selectedDeckId = null
            )
        }
    }

    fun selectDeck(id: String) {
        val deckWithCards = _deckUiState.value.decks.find { it.deck.id == id }
        if (deckWithCards != null) {
            _deckUiState.update { currentState ->
                currentState.copy(
                    selectedDeckId = id,
                    deckBottomSheetState = DeckBottomSheetState.DeckOptions(
                        deckName = deckWithCards.deck.name,
                        iconResId = deckWithCards.deck.iconResId,
                        flashcardCount = deckWithCards.flashcards.size
                    )
                )
            }
        }
    }

    fun onStudyModeClicked() {
        val id = _deckUiState.value.selectedDeckId
        val deckWithCards = _deckUiState.value.decks.find { it.deck.id == id }
        if (deckWithCards != null) {
            _deckUiState.update { currentState ->
                currentState.copy(
                    deckBottomSheetState = DeckBottomSheetState.StudyMode(
                        deckName = deckWithCards.deck.name
                    )
                )
            }
        }
    }
    fun onStudySettingsClicked() {
        _deckUiState.update { currentState ->
            currentState.copy(
                deckBottomSheetState = DeckBottomSheetState.StudySettings
            )
        }
    }

    fun onRetryErrorClicked() {
        _deckUiState.update { it.copy(deckDialogState = DeckDialogState.None) }
        startObservingData()
    }

    fun onFlashcardModeSelected(){
        val deckId = _deckUiState.value.selectedDeckId ?: return
        val deckWithCards = _deckUiState.value.decks.find {it.deck.id == deckId} ?: return

        viewModelScope.launch {
            val hasSession = studySessionRepository.doesSessionExist(deckId)

            if(hasSession){
                _deckUiState.update { currentState ->
                    currentState.copy(
                        deckDialogState = DeckDialogState.ResumeSession(deckWithCards.deck.name)
                    )
                }
            } else{
                _deckUiState.update { currentState ->
                    currentState.copy(
                        deckBottomSheetState = DeckBottomSheetState.StudySettings
                    )
                }
            }

        }
    }
}