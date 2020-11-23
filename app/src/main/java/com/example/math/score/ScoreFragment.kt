package com.example.math.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.math.R
import com.example.math.databinding.FragmentScoreBinding
import com.example.math.enums.Operation
import com.example.math.utils.*

class ScoreFragment : Fragment(R.layout.fragment_score) {
    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!
    private val args: ScoreFragmentArgs by navArgs()
    private lateinit var viewModel: ScoreViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScoreBinding.bind(view)

        val factory = ScoreViewModelFactory(args.type)
        viewModel = ViewModelProvider(this, factory).get(ScoreViewModel::class.java)

        when (args.type) {
            GAME_ONE -> {
                val game = args.game
                binding.txtQuestionScore.text = game?.question
                binding.txtAnswerScore.text = getString(R.string.right_answer, game?.answer)
                val ans = if (args.yourAnswer == -1) "" else args.yourAnswer.toString()
                binding.txtYourAnswer.text = getString(R.string.your_answer_1, ans)
            }

            GAME_TWO -> {
                binding.txtQuestionScore.setGone()
                binding.txtAnswerScore.setGone()
                binding.txtYourAnswer.setGone()
            }

            GAME_SIX -> {
                val game = args.game
                binding.txtQuestionScore.text = game?.question
                val rightAns = when (game?.operation) {
                    Operation.ADDITION -> PLUS
                    Operation.SUBTRACTION -> MINUS
                    Operation.MULTIPLICATION -> MULTIPLICATION
                    else -> DIVISION
                }
                binding.txtAnswerScore.text = getString(R.string.right_answer_6, rightAns)
                val yourAns = when (args.yourAnswer) {
                    1 -> PLUS
                    2 -> MINUS
                    3 -> MULTIPLICATION
                    else -> DIVISION
                }
                binding.txtYourAnswer.text = getString(R.string.your_answer_6, yourAns)
            }
        }

        binding.btnClosScore.setOnClickListener { findNavController().navigateUp() }
        binding.txtScore.text = args.score.toString()

        viewModel.getBastScore(args.score)

        viewModel.score.observe(viewLifecycleOwner, {
            binding.txtBestScore.text = it.toString()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}