package com.example.math.one

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.math.Game
import com.example.math.GameUtil
import com.example.math.utils.PrefManager
import com.example.math.enums.AnswerStatus
import com.example.math.utils.TOTAL_SCORE_GAME_ONE
import com.example.math.utils.ONE_SEC
import com.example.math.utils.TIMER_GAME_ONE
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class GameOneViewModel : ViewModel() {
    private var timer: Timer? = null

    private val _time = MutableLiveData(TIMER_GAME_ONE)
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

    fun chooseAnswer(selectedAns: Int) {
        if (answerStatus.value != null) return
        if (selectedAns == game.value?.answer) {
            _point.value = _point.value?.plus(20)
            PrefManager.addTotalScore(TOTAL_SCORE_GAME_ONE, 20)
            _status.value = AnswerStatus.CORRECT
        } else {
            _status.value = AnswerStatus.INCORRECT
        }

        clearTimer()
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
            /*if (timer != null) {
                clearTimer()
                _time.postValue(10)
                _status.value = null
            }*/
            _time.postValue(10)
            _status.value = null
            timer = Timer()
            _game.value = GameUtil.generateGame()
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
    }

    fun setFinished() {
        viewModelScope.launch(IO) {
            delay(500)
            _isFinished.postValue(true)
        }
    }

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