package com.capstone.arabicmorph.gamification.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LearnedWordDao {

    @Insert
    suspend fun insert(learnedWord: LearnedWord)

    @Query("SELECT COUNT(*) FROM learned_words WHERE dateLearned >= :startOfDay AND dateLearned < :endOfDay")
    suspend fun getLearnedWordsCountToday(startOfDay: Long, endOfDay: Long): Int

    @Query("SELECT * FROM learned_words ORDER BY dateLearned DESC")
    suspend fun getAllLearnedWords(): List<LearnedWord>
}
