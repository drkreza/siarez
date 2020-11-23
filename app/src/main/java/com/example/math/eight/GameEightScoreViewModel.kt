package com.example.math.eight

import androidx.lifecycle.ViewModel
import com.example.math.utils.BEST_SCORE_GAME_EIGHT
import com.example.math.utils.PrefManager

class GameEightScoreViewModel : ViewModel() {

    fun getBestScore(type:String, score:Int): Int {
        return PrefManager.timeBestScore(type, score)
    }
}