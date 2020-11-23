package com.example.math.ten

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.math.Game
import com.example.math.GameUtil
import com.example.math.enums.AnswerStatus
import com.example.math.enums.GameStatus
import com.example.math.utils.PrefManager
import com.example.math.utils.TOTAL_SCORE_GAME_TEN
import com.example.math.utils.TOTAL_SCORE_GAME_TWO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameTenViewModel : ViewModel() {
    private lateinit var games: ArrayList<Game>

    private val _questions = MutableLiveData<HashMap<String, Game>>()
    val questions: LiveData<HashMap<String, Game>> get() = _questions

    private val _answers = MutableLiveData<HashMap<String, Game>>()
    val answers: LiveData<HashMap<String, Game>> get() = _answers

    private val _answerStatus = MutableLiveData<Pair<AnswerStatus, ArrayList<Game>>>()
    val answerStatus: LiveData<Pair<AnswerStatus, ArrayList<Game>>> get() = _answerStatus

    private val _gameStatus = MutableLiveData<GameStatus>()
    val gameStatus: LiveData<GameStatus> get() = _gameStatus

    val correctCount = MutableLiveData(0)

    fun loadQuestions() {
        viewModelScope.launch(Dispatchers.Default) {
            games = GameUtil.generateGames(8)
            val questions = hashMapOf<String, Game>()
            val answers = hashMapOf<String, Game>()
            for (game in games) {
                questions[game.question] = game
                answers[game.answer.toString()] = game
            }
            _questions.postValue(questions)
            _answers.postValue(answers)
        }
    }

    fun addScore() {
        PrefManager.addTotalScore(TOTAL_SCORE_GAME_TEN, 40)
        _gameStatus.value = GameStatus.FINISHED
    }
}