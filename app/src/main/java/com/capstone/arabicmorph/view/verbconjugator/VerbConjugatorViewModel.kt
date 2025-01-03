package com.capstone.arabicmorph.view.verbconjugator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.arabicmorph.data.ConjugatorResponse
import kotlinx.coroutines.launch

class ConjugatorViewModel(private val repository: ConjugatorRepository) : ViewModel() {

    private val _conjugationResult = MutableLiveData<ConjugatorResponse?>()
    val conjugationResult: LiveData<ConjugatorResponse?> = _conjugationResult

    fun getConjugationResults(haraka: String = "u", transitive: Int = 1) {
        viewModelScope.launch {
            repository.getConjugationResults(haraka, transitive.toString()) { result ->
                _conjugationResult.postValue(result)
            }
        }
    }

}



