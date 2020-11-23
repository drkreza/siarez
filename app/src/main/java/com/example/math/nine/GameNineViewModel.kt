package com.example.math.nine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.math.Game
import com.example.math.GameUtil
import com.example.math.utils.DIVISION
import com.example.math.utils.MINUS
import com.example.math.utils.MULTIPLICATION
import com.example.math.utils.PLUS

class GameNineViewModel : ViewModel() {
    var games = MutableLiveData<List<Game>>()

    init {
        val games = GameUtil.generateGames(5)
        this.games.value = games
    }

    fun getAnswers(): ArrayList<String> {
        val numbers = ArrayList<String>()
        for (game in games.value!!) {
            val parts = game.question.split(PLUS, MINUS, DIVISION, MULTIPLICATION, "=")
            val firstNum = parts[0].trim()
            val secNum = parts[1].trim()
            numbers.add(firstNum)
            numbers.add(secNum)
        }

        numbers.shuffle()
        return numbers
    }
}