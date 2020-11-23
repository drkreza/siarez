package com.example.math.level

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.math.utils.*

class LevelViewModel(private val type: String) : ViewModel() {

    fun getTotalPoint(): MutableLiveData<Int> {
        val key = when (type) {
            GAME_ONE -> TOTAL_SCORE_GAME_ONE
            GAME_TWO -> TOTAL_SCORE_GAME_TWO
            GAME_THREE -> TOTAL_SCORE_GAME_THREE
            GAME_FOUR -> TOTAL_SCORE_GAME_FOUR
            GAME_SIX -> TOTAL_SCORE_GAME_SIX
            GAME_NINE -> TOTAL_SCORE_GAME_NINE
            GAME_TEN -> TOTAL_SCORE_GAME_TEN
            else -> TOTAL_SCORE_GAME_ONE
        }
        return MutableLiveData(PrefManager.getTotalScore(key))
    }
}