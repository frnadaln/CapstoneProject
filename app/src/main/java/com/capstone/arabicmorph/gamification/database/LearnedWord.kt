package com.capstone.arabicmorph.gamification.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learned_words")
data class LearnedWord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
    val dateLearned: Long
)
