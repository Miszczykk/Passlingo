package com.miszczyk.passlingo.data.local.entity

import androidx.room.TypeConverters

enum class StudyMode {FLASHCARDS, QUIZ, TYPING}

class StudyModeConverter{
    @TypeConverters
    fun fromMode(mode: StudyMode): String = mode.name

    @TypeConverters
    fun toMode(value: String): StudyMode = StudyMode.valueOf(value)
}