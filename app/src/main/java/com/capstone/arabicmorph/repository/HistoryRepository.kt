package com.capstone.arabicmorph.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.capstone.arabicmorph.view.history.historydatabase.HistoryDao
import com.capstone.arabicmorph.view.history.historydatabase.HistoryDatabase
import com.capstone.arabicmorph.view.history.historydatabase.HistoryEntity

class HistoryRepository (context: Context) {
    private val historyDao: HistoryDao = HistoryDatabase.getInstance(context).historyDao()

    fun getPredictHistory(): LiveData<List<HistoryEntity>> {
        return historyDao.getPredictHistory()
    }

    suspend fun insertPredictHistory(history: HistoryEntity) {
        historyDao.insertPredictHistory(listOf(history))
    }

    suspend fun deletePredictHistory(history: HistoryEntity) {
        historyDao.deletePredictHistory(history)
    }
}
