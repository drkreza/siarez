package com.example.math.four

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.math.Game
import com.example.math.GameUtil
import com.example.math.enums.AnswerStatus
import com.example.math.enums.GameStatus
import com.example.math.enums.QuestionStatus
import com.example.math.utils.ONE_SEC
import com.example.math.utils.PrefManager
import com.example.math.utils.TOTAL_SCORE_GAME_FOUR
import com.example.math.utils.TOTAL_SCORE_GAME_ONE
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameFourViewModel : ViewModel() {
    private val _questions = MutableLiveData<HashMap<String, Game>>()
    val questions: LiveData<HashMap<String, Game>> get() = _questions

    private lateinit var games: ArrayList<Game>

    private val lastAnswers = ArrayList<Game>()

    private val _answerStatus = MutableLiveData<Pair<AnswerStatus, ArrayList<Game>>>()
    val answerStatus: LiveData<Pair<AnswerStatus, ArrayList<Game>>> get() = _answerStatus

    var questionStatus = QuestionStatus.START

    private val _gameStatus = MutableLiveData<GameStatus>()
    val gameStatus: LiveData<GameStatus> get() = _gameStatus

    private var correctCount = 0
    var incorrectCount = 0

    fun loadQuestions() {
        viewModelScope.launch(Default) {
            games = GameUtil.generateGames(6)
            val result = hashMapOf<String, Game>()
            for (game in games) {
                result[game.question] = game
                result[game.answer.toString()] = game
            }
            _questions.postValue(result)
        }
    }

    fun checkAnswer(game: Game) {
        questionStatus = QuestionStatus.START
        viewModelScope.launch(Default) {
            questionStatus = QuestionStatus.PROCESSING
            lastAnswers.add(game)
            if (lastAnswers.size == 2) {
                delay(500)
                if (lastAnswers[0] == lastAnswers[1]) {
                    correctCount++
                    PrefManager.addTotalScore(TOTAL_SCORE_GAME_FOUR, 20)
                    _answerStatus.postValue(Pair(AnswerStatus.CORRECT, lastAnswers))
                } else {
                    incorrectCount++
                    _answerStatus.postValue(Pair(AnswerStatus.INCORRECT, lastAnswers))
                }
            }
            questionStatus = QuestionStatus.END
            if (correctCount == 6) {
                delay(ONE_SEC)
                _gameStatus.postValue(GameStatus.FINISHED)
            }
        }
    }

    fun beginNewQuestion() {
        lastAnswers.clear()
    }

    fun changeGameStatus(status: GameStatus) {
        _gameStatus.value = status
    }
}