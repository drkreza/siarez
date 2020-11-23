package com.example.math.six

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.math.Game
import com.example.math.GameUtil
import com.example.math.enums.AnswerStatus
import com.example.math.enums.GameStatus
import com.example.math.enums.Operation
import com.example.math.enums.QuestionStatus
import com.example.math.utils.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class GameSixViewModel : ViewModel() {
    private var timer: Timer? = null
    private var index = 0

    private val _time = MutableLiveData(TIMER_GAME_ONE)
    val time: LiveData<Int> get() = _time

    lateinit var questionStatus: QuestionStatus

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _questionText = MutableLiveData<String>()
    val questionText get() = _questionText

    private val _game = MutableLiveData<Game>()
    val game get() = _game

    private val _gameStatus = MutableLiveData<GameStatus>()
    val gameStatus: LiveData<GameStatus> get() = _gameStatus

    private val _answerStatus = MutableLiveData<AnswerStatus>()
    val answerStatus: LiveData<AnswerStatus> get() = _answerStatus

    init {
        loadQuestion()
    }

    private fun startTimer() {
        if (timer != null)
            clearTimer()
        timer = Timer()
        _time.postValue(TIMER_GAME_ONE)
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if (time.value == 0) {
                    clearTimer()
                    _gameStatus.postValue(GameStatus.FINISHED)
                    return
                }
                _time.postValue(time.value?.minus(1))
            }
        }, ONE_SEC, ONE_SEC)
    }


    fun loadQuestion() {
        viewModelScope.launch(IO) {
            if (index != 0)
                delay(1000)
            startTimer()
            _game.postValue(GameUtil.generateGameSix())
            index++
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearTimer()
    }

    private fun clearTimer() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    fun checkAns(operation: Operation) {
        viewModelScope.launch(IO) {
            questionStatus = QuestionStatus.START
            getUpdatedQuestionText(operation)
            clearTimer()
            if (isOk(operation) || _game.value?.operation == operation) {
                _answerStatus.postValue(AnswerStatus.CORRECT)
                _score.postValue(_score.value?.plus(1))
                PrefManager.addTotalScore(TOTAL_SCORE_GAME_SIX, 20)
            } else {
                _answerStatus.postValue(AnswerStatus.INCORRECT)
                delay(1000)
                _gameStatus.postValue(GameStatus.FINISHED)
            }
        }
    }

    private fun isOk(operation: Operation): Boolean {
        val question = _game.value?.question!!
        val parts = question.split(" ? ", " = ")
        return when (operation) {
            Operation.ADDITION -> {
                (parts[0].toInt() + parts[1].toInt()) == parts[2].toInt()
            }
            Operation.SUBTRACTION -> {
                (parts[0].toInt() - parts[1].toInt()) == parts[2].toInt()
            }
            Operation.DIVISION -> {
                (parts[0].toInt() / parts[1].toInt()) == parts[2].toInt()
            }
            else -> {
                (parts[0].toInt() * parts[1].toInt()) == parts[2].toInt()
            }
        }
    }

    private fun getUpdatedQuestionText(operation: Operation) {
        val operationText = when (operation) {
            Operation.ADDITION -> PLUS
            Operation.SUBTRACTION -> MINUS
            Operation.MULTIPLICATION -> MULTIPLICATION
            Operation.DIVISION -> DIVISION
        }
        _questionText.postValue(game.value?.question?.replace("?", operationText))
    }
}