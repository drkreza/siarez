package com.example.math.six

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.math.R
import com.example.math.databinding.FragmentGameSixBinding
import com.example.math.enums.AnswerStatus
import com.example.math.enums.GameStatus
import com.example.math.enums.Operation
import com.example.math.enums.QuestionStatus
import com.example.math.utils.*
import kotlinx.android.synthetic.main.fragment_game_six.*

class GameSixFragment : Fragment(R.layout.fragment_game_six), View.OnClickListener {
    private var _binding: FragmentGameSixBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameSixViewModel by viewModels()
    private lateinit var animation: ObjectAnimator
    private var selectedView: Button? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameSixBinding.bind(view)

        animation = ObjectAnimator.ofInt(binding.progressGame6, "progress", 1000, 0)

        binding.btnOpt1Game6.text = PLUS
        binding.btnOpt2Game6.text = MINUS
        binding.btnOpt3Game6.text = MULTIPLICATION
        binding.btnOpt4Game6.text = DIVISION

        binding.btnOpt1Game6.setOnClickListener(this)
        binding.btnOpt2Game6.setOnClickListener(this)
        binding.btnOpt3Game6.setOnClickListener(this)
        binding.btnOpt4Game6.setOnClickListener(this)


        viewModel.time.observe(viewLifecycleOwner, {
            binding.txtTimerGame6.text = it.toString()
        })

        viewModel.score.observe(viewLifecycleOwner, {
            binding.txtPointGame6.text = it.toString()
        })

        viewModel.game.observe(viewLifecycleOwner, {
            binding.txtQuestionGame6.text = it.question
            binding.txtQuestionGame6.setTextColor(getColor(R.color.blue))
            selectedView?.setBackgroundColor(getColor(R.color.blue))
            startProgressAnim()
            viewModel.questionStatus = QuestionStatus.END
        })

        viewModel.questionText.observe(viewLifecycleOwner, {
            binding.txtQuestionGame6.text = it
        })

        viewModel.gameStatus.observe(viewLifecycleOwner, {
            if (it == GameStatus.FINISHED) {
                val action =
                    GameSixFragmentDirections.actionGameSixFragmentToScoreFragment(
                        viewModel.game.value,
                        when (selectedView?.text) {
                            PLUS -> 1
                            MINUS -> 2
                            MULTIPLICATION -> 3
                            else -> 4
                        },
                        viewModel.score.value!!,
                        GAME_SIX)

                findNavController().navigate(action)
            }
        })

        viewModel.answerStatus.observe(viewLifecycleOwner, {
            when (it) {
                AnswerStatus.CORRECT -> {
                    binding.txtQuestionGame6.setTextColor(getColor(R.color.green))
                    selectedView?.setBackgroundColor(getColor(R.color.green))
                    viewModel.loadQuestion()
                }

                AnswerStatus.INCORRECT -> {
                    selectedView?.setBackgroundColor(getColor(R.color.red))
                    binding.txtQuestionGame6.setTextColor(getColor(R.color.red))
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startProgressAnim() {
        animation.duration = GAME_ONE_DURATION
        animation.start()
    }

    private fun stopProgressAnim() {
        animation.pause()
    }

    override fun onClick(view: View?) {
        if(viewModel.questionStatus == QuestionStatus.START)
            return
        stopProgressAnim()
        when (view?.id) {
            R.id.btnOpt1Game6 -> {
                selectedView = btnOpt1Game6
                viewModel.checkAns(Operation.ADDITION)
            }
            R.id.btnOpt2Game6 -> {
                selectedView = btnOpt2Game6
                viewModel.checkAns(Operation.SUBTRACTION)
            }
            R.id.btnOpt3Game6 -> {
                selectedView = btnOpt3Game6
                viewModel.checkAns(Operation.MULTIPLICATION)
            }
            R.id.btnOpt4Game6 -> {
                selectedView = btnOpt4Game6
                viewModel.checkAns(Operation.DIVISION)
            }
        }
    }
}