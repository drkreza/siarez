package com.example.math.two

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.math.R
import com.example.math.databinding.FragmentGameTwoBinding
import com.example.math.enums.AnswerStatus
import com.example.math.utils.GAME_TWO
import com.example.math.utils.format
import com.example.math.utils.getColor

class GameTwoFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentGameTwoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameTwoViewModel by viewModels()
    private var selectedView: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOpt1Two.setOnClickListener(this)
        binding.btnOpt2Two.setOnClickListener(this)
        binding.btnOpt3Two.setOnClickListener(this)
        binding.btnOpt4Two.setOnClickListener(this)

        viewModel.time.observe(viewLifecycleOwner, {
            binding.txtTimerTwo.text = it.toString()
        })

        viewModel.isFinished.observe(viewLifecycleOwner, {
            if (it) {
                val score = binding.txtPointTwo.text.toString().toInt()
                val action = GameTwoFragmentDirections.actionTwoScore(null, -1, score, GAME_TWO)
                findNavController().navigate(action)
            }
        })

        viewModel.game.observe(viewLifecycleOwner, {
            binding.btnOpt1Two.setBackgroundColor(getColor(R.color.blue))
            binding.btnOpt2Two.setBackgroundColor(getColor(R.color.blue))
            binding.btnOpt3Two.setBackgroundColor(getColor(R.color.blue))
            binding.btnOpt4Two.setBackgroundColor(getColor(R.color.blue))
            binding.txtQuestionTwo.text = it.question
            binding.btnOpt1Two.text = it.options[0].toString()
            binding.btnOpt2Two.text = it.options[1].toString()
            binding.btnOpt3Two.text = it.options[2].toString()
            binding.btnOpt4Two.text = it.options[3].toString()
            selectedView?.setBackgroundResource(R.drawable.btn_options_selector)
            binding.txtQuestionTwo.setTextColor(getColor(R.color.blue))
        })

        viewModel.answerStatus.observe(viewLifecycleOwner, {
            when (it) {
                AnswerStatus.CORRECT -> {
                    selectedView?.setBackgroundColor(getColor(R.color.green))
                    binding.txtQuestionTwo.setTextColor(getColor(R.color.green))
                    binding.txtQuestionTwo.text = binding.txtQuestionTwo.format(selectedView)
                    viewModel.nextQuestion()
                }

                AnswerStatus.INCORRECT -> {
                    selectedView?.setBackgroundColor(getColor(R.color.red))
                    binding.txtQuestionTwo.setTextColor(getColor(R.color.red))
                    binding.txtQuestionTwo.text = binding.txtQuestionTwo.format(selectedView)
                    //viewModel.setFinished()
                }
            }
        })

        viewModel.point.observe(viewLifecycleOwner, {
            binding.txtPointTwo.text = it.toString()
        })

        viewModel.correctCount.observe(viewLifecycleOwner, {
            when {
                (it % 4) == 0 || (it % 4) == -1 -> {
                    binding.imgCorrect1.isSelected = false
                    binding.imgCorrect2.isSelected = false
                    binding.imgCorrect3.isSelected = false
                }

                (it % 4) == 1 -> {
                    binding.imgCorrect1.isSelected = true
                    binding.imgCorrect2.isSelected = false
                    binding.imgCorrect3.isSelected = false
                }
                (it % 4) == 2 -> {
                    binding.imgCorrect2.isSelected = true
                }

                (it % 4) == 3 -> {
                    binding.imgCorrect3.isSelected = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnOpt1Two, R.id.btnOpt2Two, R.id.btnOpt3Two, R.id.btnOpt4Two -> {
                selectedView = when (view.id) {
                    R.id.btnOpt1Two -> binding.btnOpt1Two
                    R.id.btnOpt2Two -> binding.btnOpt2Two
                    R.id.btnOpt3Two -> binding.btnOpt3Two
                    R.id.btnOpt4Two -> binding.btnOpt4Two
                    else -> null
                }
                viewModel.chooseAnswer((view as Button).text.toString().toInt())
            }
        }
    }
}