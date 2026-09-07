package com.miszczyk.passlingo.ui.util

import androidx.annotation.DrawableRes
import com.miszczyk.passlingo.R

enum class DeckIcons(val id: Int, @DrawableRes val resId: Int){
    BEAR(id = 1, resId = R.drawable.deck_animal_bear),
    COW(id = 2, resId = R.drawable.deck_animal_cow),
    DOG(id = 3, resId = R.drawable.deck_animal_dog_walking),
    FISH(id = 4, resId = R.drawable.deck_animal_fish),
    HORSE(id = 5, resId = R.drawable.deck_animal_knight),
    OWL(id = 6, resId = R.drawable.deck_animal_owl),
    PENGUIN(id = 7, resId = R.drawable.deck_animal_penguinopithecus),
    SQUIRREL(id = 8, resId = R.drawable.deck_animal_squirrel),
    OCTOPUS(id = 9, resId = R.drawable.deck_animal_tentacle),
    ONE(id = 10, resId = R.drawable.deck_number_1),
    TWO(id = 11, resId = R.drawable.deck_number_2),
    THREE(id = 12, resId = R.drawable.deck_number_3),
    FOUR(id = 13, resId = R.drawable.deck_number_4),
    FIVE(id = 14, resId = R.drawable.deck_number_5),
    SIX(id = 15, resId = R.drawable.deck_number_6),
    CLUBS(id = 16, resId = R.drawable.deck_suit_clubs),
    DIAMONDS(id = 17, resId = R.drawable.deck_suit_diamonds),
    HEARTS(id = 18, resId = R.drawable.deck_suit_hearts),
    SPADES(id = 19, resId = R.drawable.deck_suit_spades),
    DUBAI(id = 20, resId = R.drawable.deck_world_burj_al_arab),
    ROME(id = 21, resId = R.drawable.deck_world_colosseum),
    PARIS(id = 22, resId = R.drawable.deck_world_eiffel),
    MOSCOW(id = 23, resId = R.drawable.deck_world_kremlin),
    EGYPT(id = 24, resId = R.drawable.deck_world_pyramid),
    SYDNEY(id = 25, resId = R.drawable.deck_world_sydney_opera),
    EARTH(id = 26, resId = R.drawable.deck_world_earth),
    GLOBE(id = 27, resId = R.drawable.deck_world_square_globe),
    SUITCASE(id = 28, resId = R.drawable.deck_world_suitcase);

    companion object{
        val all: List<DeckIcons> = entries

        fun findIconFromId(id: Int): DeckIcons{
            return entries.find { it.id == id } ?: BEAR
        }
    }
}