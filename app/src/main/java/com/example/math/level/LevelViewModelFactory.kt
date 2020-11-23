package com.example.math.level

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class LevelViewModelFactory(val type: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LevelViewModel::class.java)) {
            return LevelViewModel(type) as T
        }

        throw IllegalArgumentException("ViewModel not found!")
    }
}