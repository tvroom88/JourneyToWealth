package com.example.journeytowealth.ui.toprightmenu.macroeconomic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.journeytowealth.data.repository.MacroRepository

class MacroViewModelFactory(private val repository: MacroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MacroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MacroViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}