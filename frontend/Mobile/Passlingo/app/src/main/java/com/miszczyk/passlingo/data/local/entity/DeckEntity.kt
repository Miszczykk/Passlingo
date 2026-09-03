package com.miszczyk.passlingo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class DeckEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconResId: Int,
    val createdAt: Long = System.currentTimeMillis()
)