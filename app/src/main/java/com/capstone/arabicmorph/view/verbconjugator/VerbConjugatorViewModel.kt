package com.capstone.arabicmorph.view.verbconjugator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.arabicmorph.data.ConjugatorResponse
import kotlinx.coroutines.launch

class ConjugatorViewModel(private val repository: ConjugatorRepository) : ViewModel() {

    private val _conjugationResult = MutableLiveData<ConjugatorResponse?>()
    val conjugationResult: LiveData<ConjugatorResponse?> = _conjugationResult

    fun getConjugationResults(verb: String, haraka: String = "u", transitive: Boolean = true) {
        viewModelScope.launch {
            Log.d("ViewModel", "Calling getConjugationResults with verb: $verb")
            repository.getConjugationResults(verb, haraka, transitive) { result ->
                Log.d("ViewModel", "Result from repository: $result")
                _conjugationResult.postValue(result)  // Update LiveData with the result
            }
        }
    }
}



