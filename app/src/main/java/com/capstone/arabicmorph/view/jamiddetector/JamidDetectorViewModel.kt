package com.capstone.arabicmorph.view.jamiddetector

import androidx.lifecycle.*
import com.capstone.arabicmorph.data.JamidResponse
import kotlinx.coroutines.launch

class JamidDetectorViewModel : ViewModel() {
    private val repository = JamidDetectorRepository()

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
}
