package com.miszczyk.passlingo.ui.screens.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.data.repository.DeckRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val deckRepository = DeckRepository(context = application)
    private val _selectedDeckId = MutableStateFlow<String?>(value = null)
    val selectedDeckId: StateFlow<String?> = _selectedDeckId.asStateFlow()

    private val _isBottomSheetVisible = MutableStateFlow(value = false)
    val isBottomSheetVisible: StateFlow<Boolean> = _isBottomSheetVisible.asStateFlow()

    val decksState = deckRepository.allDecks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = emptyList()
    )

    fun showBottomSheet() {
        _isBottomSheetVisible.value = true
    }

    fun hideBottomSheet() {
        _selectedDeckId.value = null
        _isBottomSheetVisible.value = false
    }

    fun selectDeck(id: String) {
        _selectedDeckId.value = id
        showBottomSheet()
    }

    fun deleteDeck() {
        val deckId = _selectedDeckId.value ?: return

        viewModelScope.launch {
            runCatching {
                deckRepository.deleteDeck(deckId)
            }.onSuccess {
                hideBottomSheet()
            }.onFailure { exception ->
//                HomeDialogState.Error(exception.localizedMessage ?: "Failed to delete deck")
            }
        }
    }
}