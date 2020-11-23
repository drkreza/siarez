package com.example.math.two

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.math.Game
import com.example.math.GameUtil
import com.example.math.enums.AnswerStatus
import com.example.math.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class GameTwoViewModel : ViewModel() {

    private var timer: Timer? = null

    private val _time = MutableLiveData(TIMER_GAME_TWO)
    val time: LiveData<Int> get() = _time

    private val _point = MutableLiveData(0)
    val point: LiveData<Int> get() = _point

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> get() = _isFinished

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> get() = _game

    private val _status = MutableLiveData<AnswerStatus>()
    val answerStatus: LiveData<AnswerStatus> get() = _status

    private var isFirstTime = true

    private val _correctCount = MutableLiveData<Int>()
    val correctCount: LiveData<Int> get() = _correctCount

    fun chooseAnswer(selectedAns: Int) {
        //if (status.value != null) return
        if (selectedAns == game.value?.answer) {
            _point.value = _point.value?.plus(20)
            PrefManager.addTotalScore(TOTAL_SCORE_GAME_TWO, 20)
            if (correctCount.value?.rem(4) == 3) {
                _correctCount.value = (_correctCount.value ?: 0).plus(2)
            }else {
                _correctCount.value = (_correctCount.value ?: 0).plus(1)
            }
            _status.value = AnswerStatus.CORRECT
        } else {
            _correctCount.value = -1
            _status.value = AnswerStatus.INCORRECT
        }

        //clearTimer()
    }

    init {
        nextQuestion()
    }

    fun nextQuestion() {
        viewModelScope.launch {
            if (isFirstTime) {
                delay(0)
                isFirstTime = false
            } else {
                delay(ONE_SEC)
            }
            //_time.postValue(10)
            // _status.value = null
            if (timer == null) {
                timer = Timer()
                timer?.schedule(object : TimerTask() {
                    override fun run() {
                        if (_time.value == 0) {
                            _isFinished.postValue(true)
                            clearTimer()
                            return
                        }

                        _time.postValue(_time.value?.minus(1))
                    }
                }, ONE_SEC, ONE_SEC)
            }
            _game.value = GameUtil.generateGame()
        }
    }

    /*fun setFinished() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            _isFinished.postValue(true)
        }
    }*/

    private fun clearTimer() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    override fun onCleared() {
        clearTimer()
        super.onCleared()
    }
}