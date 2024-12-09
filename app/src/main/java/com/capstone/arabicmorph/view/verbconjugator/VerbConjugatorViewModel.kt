package com.capstone.arabicmorph.view.verbconjugator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.arabicmorph.data.ConjugatorResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class ConjugatorViewModel(private val repository: ConjugatorRepository) : ViewModel() {

    private val _conjugatorResponse = MutableLiveData<ConjugatorResponse>()
    val conjugatorResponse: LiveData<ConjugatorResponse> = _conjugatorResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getConjugationResults(verb: String) {
        viewModelScope.launch {
            try {
                val response: Response<ConjugatorResponse> = repository.getConjugationResults(verb)
                if (response.isSuccessful) {
                    _conjugatorResponse.value = response.body()
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }
}
