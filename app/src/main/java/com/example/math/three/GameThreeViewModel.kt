package com.example.math.three

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.math.Game
import com.example.math.GameUtil
import com.example.math.enums.AnswerStatus
import com.example.math.utils.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameThreeViewModel : ViewModel() {
    private val _questionText = MutableLiveData<String>()
    val questionText: LiveData<String> get() = _questionText

    private lateinit var game: Game
    private var isFirstTime = true

    private val _status = MutableLiveData<AnswerStatus>()
    val answerStatus: LiveData<AnswerStatus> get() = _status

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> get() = _isFinished

    private var errors = ArrayList<Game>()

    var index = 0

    init {
        nextQuestion()
    }

    fun nextQuestion() {
        viewModelScope.launch(IO) {
            index++
            if (isFirstTime) {
                delay(0)
                isFirstTime = false
            } else {
                delay(ONE_SEC)
            }
            if (index == 21) {
                _isFinished.postValue(true)
                return@launch
            }
            game = GameUtil.generateGame()
            _questionText.postValue(game.question)
        }
    }

    fun attach(tag: String?) {
        _questionText.value = questionText.value?.attach(tag)
    }

    fun detach() {
        _questionText.value = questionText.value?.detach()
    }

    fun checkAnswer() {
        if (questionText.value?.getSelectedAnswer() == game.answer) {
            _status.value = AnswerStatus.CORRECT
            PrefManager.addTotalScore(TOTAL_SCORE_GAME_THREE, 10)
        } else {
            _status.value = AnswerStatus.INCORRECT
            errors.add(game)
        }
    }

    fun getErrors(): ArrayList<Game> {
        return errors
    }
}