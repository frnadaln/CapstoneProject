package com.capstone.arabicmorph.view.history

import android.app.Application
import androidx.lifecycle.*
import com.capstone.arabicmorph.repository.HistoryRepository
import com.capstone.arabicmorph.view.history.historydatabase.HistoryEntity
import kotlinx.coroutines.launch

class HistoryViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: HistoryRepository = HistoryRepository(application)
    val predictHistory: LiveData<List<HistoryEntity>> = repository.getPredictHistory()

    fun savePrediction(history: HistoryEntity) {
        viewModelScope.launch {
            repository.insertPredictHistory(history)
        }
    }

    fun deletePrediction(history: HistoryEntity) {
        viewModelScope.launch {
            repository.deletePredictHistory(history)
        }
    }
}
