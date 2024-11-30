package com.capstone.arabicmorph.repository

import com.capstone.arabicmorph.gamification.database.LearnedWord
import com.capstone.arabicmorph.gamification.database.LearnedWordDao
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LearnedWordRepository(private val learnedWordDao: LearnedWordDao) {

    @OptIn(DelicateCoroutinesApi::class)
    fun addLearnedWord(word: String) {
        val learnedWord = LearnedWord(word = word, dateLearned = System.currentTimeMillis())
        GlobalScope.launch(Dispatchers.IO) {
            learnedWordDao.insert(learnedWord)
        }
    }
}
