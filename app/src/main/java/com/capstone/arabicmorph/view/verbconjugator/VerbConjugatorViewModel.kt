package com.capstone.arabicmorph.view.verbconjugator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.arabicmorph.data.ConjugatorResponse
import kotlinx.coroutines.launch

class ConjugatorViewModel(private val repository: ConjugatorRepository) : ViewModel() {

    private val _conjugatorResponse = MutableLiveData<ConjugatorResponse>()
    val conjugatorResponse: LiveData<ConjugatorResponse> = _conjugatorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun getConjugationResults(verb: String) {
        viewModelScope.launch {
            _isLoading.value = true // Emit loading state
            try {
                val response = repository.getConjugationResults(verb)
                if (response != null) {
                    if (response.isSuccessful) {
                        _conjugatorResponse.value = response.body()
                    } else {
                        _errorMessage.value = "Error: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}"
            } finally {
                _isLoading.value = false // Stop loading state
            }
        }
    }
}
