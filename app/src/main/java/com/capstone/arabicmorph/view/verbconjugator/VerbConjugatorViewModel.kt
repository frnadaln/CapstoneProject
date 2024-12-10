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

    fun getConjugationResults(verb: String) {
        viewModelScope.launch {
            try {
                val response = repository.getConjugationResults(verb)
                if (response.isSuccessful) {
                    _conjugatorResponse.value = response.body()
                }
            } catch (e: Exception) {
                //
            }
        }
    }
}