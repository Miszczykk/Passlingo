package com.miszczyk.passlingo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "study_card_progress",
    foreignKeys = [
        ForeignKey(entity = StudySessionEntity::class, parentColumns = ["id"], childColumns = ["sessionId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = FlashcardEntity::class, parentColumns = ["id"], childColumns = ["flashcardId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [
        Index("flashcardId"),
        Index(value = ["sessionId", "currentRound", "orderIndex"])
    ]
)
data class StudyCardProgressEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val flashcardId: String,
    val currentRound: Int,
    val attempts: Int,
    val orderIndex: Int
)