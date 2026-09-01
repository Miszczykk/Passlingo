package com.miszczyk.passlingo.ui.screens.createDeck.viewmodel

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckDialogState
import com.miszczyk.passlingo.ui.screens.createDeck.model.CreateDeckUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class CreateDeckViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(CreateDeckUiState())
    val uiState: StateFlow<CreateDeckUiState> = _uiState.asStateFlow()
//    val deckNameState = TextFieldState("")

    private val _navigateBack = Channel<Unit>(Channel.BUFFERED)
    val navigateBack = _navigateBack.receiveAsFlow()

    private val dialogAction = CreateDeckDialogAction(_uiState, viewModelScope, _navigateBack)

    val frontCardState = TextFieldState("")
    val backCardState = TextFieldState("")

    val deckIconsList = listOf(
        R.drawable.deck_animal_bear,
        R.drawable.deck_animal_cow,
        R.drawable.deck_animal_dog_walking,
        R.drawable.deck_animal_fish,
        R.drawable.deck_animal_knight,
        R.drawable.deck_animal_owl,
        R.drawable.deck_animal_penguinopithecus,
        R.drawable.deck_animal_squirrel,
        R.drawable.deck_animal_tentacle,
        R.drawable.deck_number_1,
        R.drawable.deck_number_2,
        R.drawable.deck_number_3,
        R.drawable.deck_number_4,
        R.drawable.deck_number_5,
        R.drawable.deck_number_6,
        R.drawable.deck_suit_clubs,
        R.drawable.deck_suit_diamonds,
        R.drawable.deck_suit_hearts,
        R.drawable.deck_suit_spades,
        R.drawable.deck_world_burj_al_arab,
        R.drawable.deck_world_colosseum,
        R.drawable.deck_world_earth,
        R.drawable.deck_world_eiffel,
        R.drawable.deck_world_kremlin,
        R.drawable.deck_world_pyramid,
        R.drawable.deck_world_square_globe,
        R.drawable.deck_world_suitcase,
        R.drawable.deck_world_sydney_opera
    )


    fun onDialogCancelled() = dialogAction.onDialogCancelled()
    fun onDialogConfirmed() = dialogAction.onDialogConfirmed()

    fun onSaveDeckClicked(deckName: String, addedCards: Int) {
        if (deckName.isNotBlank() && addedCards > 0) {
            _uiState.update { it.copy(dialogState = CreateDeckDialogState.SaveDeck) }
        }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(dialogState = CreateDeckDialogState.DiscardChanges) }
    }

    fun onSelectIconClicked() {
        _uiState.update { it.copy(showBottomSheet = true) }
    }

    fun onIconClicked(selectedIcon: Int) {
        _uiState.update { it.copy(deckIcon = selectedIcon) }
        onSheetDismissed()
    }

    fun onAddToDeckClicked(addedCards: Int) {
        if (frontCardState.text.toString().isNotBlank() && backCardState.text.toString()
                .isNotBlank()
        ) {
            _uiState.update { it.copy(addedCards = addedCards + 1) }

            frontCardState.edit { replace(0, length, "") }
            backCardState.edit { replace(0, length, "") }
        } else {
            _uiState.update { it.copy(dialogState = CreateDeckDialogState.Error("Please fill in both the Front and Back of the flashcard before adding.")) }
        }
    }


    fun onSheetDismissed() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }
}