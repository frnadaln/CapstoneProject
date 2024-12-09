package com.capstone.arabicmorph.view.jamiddetector

import android.app.Application
import androidx.lifecycle.*
import com.capstone.arabicmorph.data.JamidResponse
import com.capstone.arabicmorph.view.history.historydatabase.HistoryDatabase
import com.capstone.arabicmorph.view.history.historydatabase.HistoryEntity
import kotlinx.coroutines.launch

class JamidDetectorViewModel(application: Application) : AndroidViewModel(application) {
    private val historyDao = HistoryDatabase.getInstance(application).historyDao()
    private val repository = JamidDetectorRepository(historyDao)

    private val _result = MutableLiveData<Result<JamidResponse>>()
    val result: LiveData<Result<JamidResponse>> get() = _result

    fun predictJamid(input: String) {
        viewModelScope.launch {
            try {
                val response = repository.predictJamid(input)
                _result.postValue(response)
            } catch (e: Exception) {
                _result.postValue(Result.failure(e))
            }
        }
    }

    fun savePrediction(history: HistoryEntity) {
        viewModelScope.launch {
            repository.insertPredictHistory(history)
        }
    }
}
