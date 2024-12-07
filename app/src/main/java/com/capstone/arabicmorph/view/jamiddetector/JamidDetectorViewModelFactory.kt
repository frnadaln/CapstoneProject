package com.capstone.arabicmorph.view.jamiddetector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JamidDetectorViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(JamidDetectorViewModel::class.java) -> {
                JamidDetectorViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
