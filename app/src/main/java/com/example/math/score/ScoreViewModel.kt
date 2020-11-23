package com.example.math.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.math.utils.*

class ScoreViewModel(private val type: String) : ViewModel() {
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score

    fun getBastScore(score: Int) {
        val key = when (type) {
            GAME_ONE -> BEST_SCORE_GAME_ONE
            GAME_TWO -> BEST_SCORE_GAME_TWO
            else -> BEST_SCORE_GAME_SIX
        }
        val bestScore = PrefManager.pointBestScore(key, score)
        _score.value = bestScore
    }
}