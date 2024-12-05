package com.capstone.arabicmorph.view.jamiddetector

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.arabicmorph.data.JamidResponse
import kotlinx.coroutines.launch

class JamidDetectorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = JamidDetectorRepository()
    private val _result = MutableLiveData<Result<JamidResponse>>()
    val result: LiveData<Result<JamidResponse>> = _result

    fun predictJamid(input: String) {
        viewModelScope.launch {
            _result.value = repository.predictJamid(input)
        }
    }
}
