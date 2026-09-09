package com.miszczyk.passlingo.ui.screens.home.model.deck

sealed interface DeckBottomSheetState {
    data object None : DeckBottomSheetState
    data class DeckOptions(override val deckName: String, val iconResId: Int, val flashcardCount: Int) : DeckBottomSheetState, HasDeckName
    data class StudyMode(override val deckName: String) : DeckBottomSheetState, HasDeckName
    data object StudySettings : DeckBottomSheetState
}