package com.example.math.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.math.level.LevelViewModel
import java.lang.IllegalArgumentException

class ScoreViewModelFactory(val type: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(type) as T
        }

        throw IllegalArgumentException("ViewModel not found!")
    }
}