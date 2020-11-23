package com.example.math

import com.example.math.enums.Operation
import com.example.math.utils.*
import java.util.*
import kotlin.collections.ArrayList

object GameUtil {
    fun generateGame(): Game {
        val operation = randomOperation()
        var num1 = rand(1)
        var num2 = rand(1)
        var answer = -1
        val operationText = when (operation) {
            Operation.ADDITION -> {
                answer = num1 + num2
                PLUS
            }
            Operation.SUBTRACTION -> {
                num1 = randBetween()
                num2 = if (num1.digitCount() > 1) rand(1) else randBetween()
                if (num1 < num2) {
                    val x = num1
                    num1 = num2
                    num2 = x
                }
                answer = num1 - num2
                MINUS
            }
            Operation.MULTIPLICATION -> {
                answer = num1 * num2
                MULTIPLICATION
            }
            Operation.DIVISION -> {
                while (num1 % num2 != 0 && num2 % num1 != 0) {
                    num1 = randBetween()
                    num2 = randBetween()
                }

                if (num1 < num2) {
                    val x = num1
                    num1 = num2
                    num2 = x
                }

                answer = num1 / num2
                DIVISION
            }
        }
        val options = generateOptions(answer)
        val title = "$num1 $operationText $num2 = ?"
        return Game(title, options, operation, answer)
    }

    private fun rand(digits: Int): Int {
        val minimum = Math.pow(10.0, digits - 1.0).toInt()
        val maximum = Math.pow(10.0, digits.toDouble()).toInt() - 1
        return minimum + Random().nextInt(maximum - minimum + 1)
    }

    private fun randBetween(min: Int = 1, max: Int = 30): Int {
        return Random().nextInt(max - min + 1) + min
    }

    private fun randomOperation(start: Int = 1, end: Int = 4): Operation {
        val random = randBetween(start, end)
        var operation: Operation? = null

        when (random) {
            1 -> operation = Operation.ADDITION
            2 -> operation = Operation.SUBTRACTION
            3 -> operation = Operation.MULTIPLICATION
            4 -> operation = Operation.DIVISION
        }

        return operation!!
    }

    private fun generateOptions(answer: Int): List<Int> {
        val answers = arrayListOf(answer)
        var count = 0

        while (count < 3) {
            val randomOperation = randBetween(1, 3)
            val randomNumber = randBetween(1, 4)
            val newNumber = when (randomOperation) {
                1 -> if (answer > randomNumber) answer - randomNumber else randomNumber - answer
                else -> answer + randomNumber
            }
            if (!answers.contains(newNumber)) {
                answers.add(newNumber)
                count++
            }
        }
        answers.shuffle()
        return answers
    }

    fun generateGames(count: Int): ArrayList<Game> {
        val games = arrayListOf<Game>()
        var index = 0

        while (index < count) {
            val game = generateGame()
            if (games.isEmpty()) {
                games.add(game)
                index++
                continue
            }

            var mustBeAdded = true
            for (item in games) {
                if (game.question == item.question || game.answer == item.answer) {
                    mustBeAdded = false
                }
            }

            if (mustBeAdded) {
                games.add(game)
                index++
            }
        }
        return games
    }


    fun generateGameFiveNumbers(count: Int): ArrayList<Int> {
        val numbers = arrayListOf<Int>()
        for (i in 1..count)
            numbers.add(rand(1))
        return numbers
    }

    fun generateGameSix(): Game {
        val game = generateGame()
        val newQuestion =
            game.question.replace("?", game.answer.toString()).replace(PLUS, "?").replace(MINUS, "?").replace(
                MULTIPLICATION, "?").replace(DIVISION, "?")
        game.question = newQuestion
        return game
    }

    fun generateEightGame(): Game {
        var game = Game("", emptyList(), Operation.MULTIPLICATION, 0)
        while (game.operation == Operation.DIVISION || game.operation == Operation.MULTIPLICATION) {
            game = generateGame()
        }

        val question = game.question
        val parts = question.split(" ")
        val number = parts[0]
        val prevOperation = parts[1]
        val preNumber = parts[2]
        val newOperation = if (number.length > 1)
            Operation.MULTIPLICATION
        else
            randomOperation(3, 4)

        val answer = game.answer

        var newFirstPart = ""
        var newResult = -1

        when (newOperation) {
            Operation.MULTIPLICATION -> {
                var randomNum = rand(1)
                while (number.toInt() % randomNum != 0) {
                    randomNum = rand(1)
                }

                newFirstPart = "${randomNum}x"
                newResult = number.toInt() / randomNum
            }

            Operation.DIVISION -> {
                val randomNum = rand(1)
                newFirstPart = "x $DIVISION $randomNum"
                newResult = number.toInt() * randomNum
            }
        }
        game.question = "$newFirstPart $prevOperation $preNumber = $answer"
        game.answer = newResult
        return game
    }
}

