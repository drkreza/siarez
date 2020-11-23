package com.example.math.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.math.utils.*

class ResetViewModel : ViewModel() {
    private val _isReset = MutableLiveData<Boolean>()
    val isReset: LiveData<Boolean> get() = _isReset

    fun resetProgress(type: String) {
        val key = when (type) {
            GAME_ONE -> TOTAL_SCORE_GAME_ONE
            GAME_TWO -> TOTAL_SCORE_GAME_TWO
            GAME_THREE -> TOTAL_SCORE_GAME_THREE
            GAME_FOUR -> TOTAL_SCORE_GAME_FOUR
            GAME_SIX -> TOTAL_SCORE_GAME_SIX
            GAME_NINE -> TOTAL_SCORE_GAME_NINE
            else -> TOTAL_SCORE_GAME_TEN
        }
        PrefManager.resetProgress(key)
        _isReset.value = true
    }
}