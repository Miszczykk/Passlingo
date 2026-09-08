package com.miszczyk.passlingo.ui.screens.home.viewmodel.deck

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.data.repository.DeckRepository
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckDialogState
import com.miszczyk.passlingo.ui.screens.home.model.deck.DeckUiState
import com.miszczyk.passlingo.ui.util.observeWithRetry
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DeckViewModel(application: Application) : AndroidViewModel(application) {
    private val deckRepository = DeckRepository(context = application)
    private val _deckUiState = MutableStateFlow(value = DeckUiState())
    val deckUiState: StateFlow<DeckUiState> = _deckUiState.asStateFlow()

    private val deckDialogAction = DeckDialogAction(uiStateFlow = _deckUiState, externalScope = viewModelScope, deckRepository = deckRepository)
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

    fun deleteDeck() = deckSelectionAction.onDeleteDeckClicked()

    fun hideBottomSheet() {
        _deckUiState.update { currentState ->
            currentState.copy(
                showBottomSheet = false,
                selectedDeckId = null
            )
        }
    }

    fun selectDeck(id: String) {
        _deckUiState.update { currentState ->
            currentState.copy(
                selectedDeckId = id,
                showBottomSheet = true
            )
        }
    }

    fun onStudyModeClicked(){
        _deckUiState.update { currentState ->
            currentState.copy(
                showBottomSheet = false,
                showStudyModeBottomSheet = true
            )
        }
    }
    fun hideStudyModeBottomSheet(){
        _deckUiState.update { currentState ->
            currentState.copy(
                showStudyModeBottomSheet = false,
                selectedDeckId = null
            )
        }
    }

    fun onRetryErrorClicked() {
        _deckUiState.update { it.copy(deckDialogState = DeckDialogState.None) }
        startObservingData()
    }
}