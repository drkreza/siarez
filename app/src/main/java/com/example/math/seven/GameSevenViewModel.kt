package com.example.math.seven

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.math.Game
import com.example.math.GameUtil
import com.example.math.IdValue
import com.example.math.enums.AnswerStatus
import com.example.math.enums.GameStatus
import com.example.math.enums.QuestionStatus
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameSevenViewModel : ViewModel() {

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> get() = _game

    private val _answerStatus = MutableLiveData<AnswerStatus>()
    val answerStatus: LiveData<AnswerStatus> get() = _answerStatus

    private val _questionStatus = MutableLiveData<QuestionStatus>()
    val questionStatus: LiveData<QuestionStatus> get() = _questionStatus

    private val _gameStatus = MutableLiveData<GameStatus>()
    val gameStatus: LiveData<GameStatus> get() = _gameStatus

    init {
        _game.value = GameUtil.generateGame()
    }

    fun checkAnswer(valueId: IdValue, type: String) {
        if (valueId.value == game.value?.answer) {
            _answerStatus.value = AnswerStatus.CORRECT
        } else {
            _answerStatus.value = AnswerStatus.INCORRECT
        }
    }

    fun loadNextQuestion() {
        viewModelScope.launch(IO) {
            _questionStatus.postValue(QuestionStatus.PROCESSING)
            delay(1000)
            _game.postValue(GameUtil.generateGame())
            _questionStatus.postValue(QuestionStatus.START)
        }
    }

    fun setFinished() {
        viewModelScope.launch(IO) {
            delay(1000)
            _gameStatus.postValue(GameStatus.FINISHED)
        }
    }
}