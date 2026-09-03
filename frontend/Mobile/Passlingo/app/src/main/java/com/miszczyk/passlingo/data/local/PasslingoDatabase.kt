package com.miszczyk.passlingo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.miszczyk.passlingo.data.local.dao.DeckDao
import com.miszczyk.passlingo.data.local.entity.DeckEntity
import com.miszczyk.passlingo.data.local.entity.FlashcardEntity

@Database(
    entities = [DeckEntity::class, FlashcardEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PasslingoDatabase : RoomDatabase(){
    abstract fun deckDao(): DeckDao

    companion object{
        @Volatile
        private var instance: PasslingoDatabase? = null

        fun getInstance(context: Context): PasslingoDatabase{
            return instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    PasslingoDatabase::class.java,
                    "passlingo_database"
                ).build().also { instance = it }
            }
        }
    }
}