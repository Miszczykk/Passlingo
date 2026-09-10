package com.miszczyk.passlingo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "study_sessions",
    foreignKeys = [ForeignKey(
        entity = DeckEntity::class,
        parentColumns = ["id"],
        childColumns = ["deckId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("deckId", unique = true)]
)
data class StudySessionEntity(
    @PrimaryKey val id: String,
    val deckId: String,
    val mode: StudyMode,
    val targetRounds: Int,
)