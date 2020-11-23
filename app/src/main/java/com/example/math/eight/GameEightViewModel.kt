package com.example.math.eight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.math.Game
import com.example.math.GameUtil
import com.example.math.enums.AnswerStatus
import com.example.math.enums.GameStatus
import com.example.math.utils.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import java.util.*

class GameEightViewModel : ViewModel() {
    private var timer: Timer? = null
    private var isFirstTime = true

    private val _time = MutableLiveData(0)
    val time: LiveData<Int> get() = _time

    private val _questionText = MutableLiveData<String>()
    val questionText: LiveData<String> get() = _questionText

    private val _gameStatus = MutableLiveData<GameStatus>()
    val gameStatus: LiveData<GameStatus> get() = _gameStatus

    private val _answerText = MutableLiveData("x = ?")
    val answerText: LiveData<String> get() = _answerText

    private lateinit var game: Game

    private var correctAns = 0

    private val _status = MutableLiveData<AnswerStatus>()
    val answerStatus: LiveData<AnswerStatus> get() = _status

    init {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                _time.postValue(time.value?.plus(1))
            }
        }, ONE_SEC, ONE_SEC)

        loadNextQuestion()
    }

    fun loadNextQuestion() {
        viewModelScope.launch(IO) {
            if (isFirstTime)
                isFirstTime = !isFirstTime
            else
                delay(ONE_SEC)
            if (correctAns == 5) {
                _gameStatus.postValue(GameStatus.FINISHED)
                clearTimer()
                PrefManager.timeBestScore(BEST_SCORE_GAME_EIGHT, time.value!!)
                return@launch
            }
            _answerText.postValue("x = ?")
            game = GameUtil.generateEightGame()
            _questionText.postValue(game.question)
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

    fun attach(tag: String?) {
        _answerText.value = answerText.value?.attach(tag)
    }

    fun detach() {
        _answerText.value = answerText.value?.detach()
    }

    fun checkAnswer() {
        if(answerText.value == "x = ?")
            return
        if (answerText.value?.getSelectedAnswer() == game.answer) {
            _status.value = AnswerStatus.CORRECT
            correctAns++
        } else {
            _status.value = AnswerStatus.INCORRECT
        }
    }
}