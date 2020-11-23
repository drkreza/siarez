package com.example.math.utils

import androidx.preference.PreferenceManager
import com.example.math.G

class PrefManager {

    companion object {
        private var prefs = PreferenceManager.getDefaultSharedPreferences(G.context)

        fun getTotalScore(key: String): Int {
            return prefs.getInt(key, 0)
        }

        fun addTotalScore(key: String, point: Int) {
            var oldPoints = prefs.getInt(key, 0)
            oldPoints += point
            prefs.edit().putInt(key, oldPoints).apply()
        }

        fun pointBestScore(key: String, score: Int): Int {
            val lastBestScore = prefs.getInt(key, 0)
            if (score > lastBestScore) {
                prefs.edit().putInt(key, score).apply()
                return score
            }
            return lastBestScore
        }

        fun timeBestScore(key: String, score: Int): Int {
            val lastBestScore = prefs.getInt(key, Int.MAX_VALUE)
            if (score < lastBestScore) {
                prefs.edit().putInt(key, score).apply()
                return score
            }
            return lastBestScore
        }

        fun resetProgress(key: String) {
            prefs.edit().remove(key).apply()
        }
    }
}