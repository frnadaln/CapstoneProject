package com.capstone.arabicmorph.view.verbconjugator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.arabicmorph.data.ConjugatorRepository

class ConjugatorViewModelFactory(private val repository: ConjugatorRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConjugatorViewModel::class.java)) {
            return ConjugatorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
