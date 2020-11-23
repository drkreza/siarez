package com.example.math.five

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class GameFiveViewModelFactory(val level: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameFiveViewModel::class.java)) {
            return GameFiveViewModel(level) as T
        }
        throw IllegalArgumentException("viewModel not found!")
    }
}