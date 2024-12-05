package com.capstone.arabicmorph.view.jamiddetector

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JamidDetectorViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JamidDetectorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JamidDetectorViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
