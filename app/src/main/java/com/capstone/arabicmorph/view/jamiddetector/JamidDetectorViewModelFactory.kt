package com.capstone.arabicmorph.view.jamiddetector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JamidDetectorViewModelFactory(private val repository: JamidDetectorRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JamidDetectorViewModel::class.java)) {
            return JamidDetectorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
