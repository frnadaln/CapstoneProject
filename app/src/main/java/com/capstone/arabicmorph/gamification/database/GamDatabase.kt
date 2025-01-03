package com.capstone.arabicmorph.gamification.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LearnedWord::class], version = 1)
abstract class GamDatabase : RoomDatabase() {
    abstract fun learnedWordDao(): LearnedWordDao

    companion object {
        @Volatile
        private var instance: GamDatabase? = null

        fun getInstance(context: Context): GamDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    GamDatabase::class.java,
                    "learned_words_db"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }
}
