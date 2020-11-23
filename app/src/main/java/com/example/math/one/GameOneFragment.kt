package com.example.math.one

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.math.R
import com.example.math.databinding.FragmentGameOneBinding
import com.example.math.enums.AnswerStatus
import com.example.math.utils.GAME_ONE
import com.example.math.utils.GAME_ONE_DURATION
import com.example.math.utils.format
import com.example.math.utils.getColor

class GameOneFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentGameOneBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameOneViewModel by viewModels()
    private var selectedView: Button? = null
    private lateinit var animation: ObjectAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOpt1Game1.setOnClickListener(this)
        binding.btnOpt2Game1.setOnClickListener(this)
        binding.btnOpt3Game1.setOnClickListener(this)
        binding.btnOpt4Game1.setOnClickListener(this)
        animation = ObjectAnimator.ofInt(binding.progressGame1, "progress", 1000, 0)

        viewModel.time.observe(viewLifecycleOwner, {
            binding.txtTimerGame1.text = it.toString()
        })

        viewModel.isFinished.observe(viewLifecycleOwner, {
            if (it) {
                val score = binding.txtPointGame1.text.toString().toInt()
                val selectedAns = if(selectedView == null) -1 else selectedView?.text.toString().toInt()
                val action =
                    GameOneFragmentDirections.actionOneScore(viewModel.game.value!!, selectedAns, score, GAME_ONE)
                findNavController().navigate(action)
            }
        })

        viewModel.game.observe(viewLifecycleOwner, {
            binding.txtQuestionGame1.text = it.question
            binding.btnOpt1Game1.text = it.options[0].toString()
            binding.btnOpt2Game1.text = it.options[1].toString()
            binding.btnOpt3Game1.text = it.options[2].toString()
            binding.btnOpt4Game1.text = it.options[3].toString()
            selectedView?.setBackgroundResource(R.drawable.btn_options_selector)
            binding.txtQuestionGame1.setTextColor(getColor(R.color.blue))
            binding.progressGame1.progress = 1000
            startProgressAnim()
        })

        viewModel.answerStatus.observe(viewLifecycleOwner, {
            when (it) {
                AnswerStatus.CORRECT -> {
                    selectedView?.setBackgroundColor(getColor(R.color.green))
                    binding.txtQuestionGame1.setTextColor(getColor(R.color.green))
                    binding.txtQuestionGame1.text = binding.txtQuestionGame1.format(selectedView)
                    viewModel.nextQuestion()
                }

                AnswerStatus.INCORRECT -> {
                    selectedView?.setBackgroundColor(getColor(R.color.red))
                    binding.txtQuestionGame1.setTextColor(getColor(R.color.red))
                    binding.txtQuestionGame1.text = binding.txtQuestionGame1.format(selectedView)
                    viewModel.setFinished()
                }
            }
        })

        viewModel.point.observe(viewLifecycleOwner, {
            binding.txtPointGame1.text = it.toString()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnOpt1Game1, R.id.btnOpt2Game1, R.id.btnOpt3Game1, R.id.btnOpt4Game1 -> {
                selectedView = when (view.id) {
                    R.id.btnOpt1Game1 -> binding.btnOpt1Game1
                    R.id.btnOpt2Game1 -> binding.btnOpt2Game1
                    R.id.btnOpt3Game1 -> binding.btnOpt3Game1
                    R.id.btnOpt4Game1 -> binding.btnOpt4Game1
                    else -> null
                }
                stopProgressAnim()
                viewModel.chooseAnswer((view as Button).text.toString().toInt())
            }
        }
    }

    private fun startProgressAnim() {
        animation.duration = GAME_ONE_DURATION
        animation.start()
    }

    private fun stopProgressAnim() {
        animation.pause()
    }
}