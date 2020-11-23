package com.example.math.five

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.math.GameUtil
import com.example.math.IdValue
import com.example.math.enums.AnswerStatus
import com.example.math.enums.GameStatus
import com.example.math.utils.ONE_SEC
import java.util.*
import kotlin.collections.ArrayList

class GameFiveViewModel(val level: Int) : ViewModel() {
    private var timer: Timer? = null
    private var index = 0

    private var selectedAns = ArrayList<IdValue>()
    private var answers: List<List<Int>>

    private var _time = MutableLiveData<Long>(0)
    val time: LiveData<Long> get() = _time

    private val _numbers = MutableLiveData<ArrayList<Int>>()
    val numbers: LiveData<ArrayList<Int>> get() = _numbers

    private val _question = MutableLiveData<Int>()
    val question: LiveData<Int> get() = _question

    private val _answerStatus = MutableLiveData<AnswerStatus>()
    val answerStatus: LiveData<AnswerStatus> get() = _answerStatus

    private val _gameStatus = MutableLiveData<GameStatus>()
    val gameStatus: LiveData<GameStatus> get() = _gameStatus

    init {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                _time.postValue((time.value ?: 0).plus(1))
            }
        }, ONE_SEC, ONE_SEC)

        _numbers.value = GameUtil.generateGameFiveNumbers(level * level)
        answers = generateAnswers()
    }

    override fun onCleared() {
        super.onCleared()
        clearTimer()
    }

    fun loadQuestion() {
        if (index == level) {
            _gameStatus.value = GameStatus.FINISHED
            clearTimer()
            return
        }
        selectedAns.clear()
        _question.value = answers[index++].sum()
    }

    private fun clearTimer() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    private fun generateAnswers(): List<List<Int>> {
        val shuffleNumbers = ArrayList(numbers.value)
        shuffleNumbers.shuffle()
        return shuffleNumbers.chunked(level)
    }

    fun chooseOptions(idValue: IdValue) {
        if (selectedAns.contains(idValue))
            selectedAns.remove(idValue)
        else
            selectedAns.add(idValue)

        if (selectedAns.map { it.value }.sum() == question.value)
            _answerStatus.value = AnswerStatus.CORRECT
    }

    fun getSelectedAns(): ArrayList<IdValue> {
        return selectedAns
    }

    fun gameFinished() {
        _gameStatus.value = GameStatus.STARTED
    }
}