package com.example.math.seven

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.math.IdValue
import com.example.math.R
import com.example.math.databinding.FragmentGameSevenBinding
import com.example.math.enums.AnswerStatus
import com.example.math.enums.GameStatus
import com.example.math.enums.QuestionStatus
import com.example.math.utils.ONE_SEC
import com.example.math.utils.format
import com.example.math.utils.getColor

class GameSevenFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentGameSevenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameSevenViewModel by viewModels()
    private lateinit var valueId: IdValue
    private var topProgress = 0
    private var downProgress = 0
    private lateinit var animationTop: ObjectAnimator
    private lateinit var animationDown: ObjectAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameSevenBinding.inflate(inflater, container, false)
        binding.btnOpt1Game7Top.setOnClickListener(this)
        binding.btnOpt2Game7Top.setOnClickListener(this)
        binding.btnOpt3Game7Top.setOnClickListener(this)
        binding.btnOpt4Game7Top.setOnClickListener(this)
        binding.btnOpt1Game7Down.setOnClickListener(this)
        binding.btnOpt2Game7Down.setOnClickListener(this)
        binding.btnOpt3Game7Down.setOnClickListener(this)
        binding.btnOpt4Game7Down.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.game.observe(viewLifecycleOwner, {
            resetViewStyle()
            binding.btnOpt1Game7Top.text = it.options[0].toString()
            binding.btnOpt1Game7Down.text = it.options[0].toString()
            binding.btnOpt2Game7Top.text = it.options[1].toString()
            binding.btnOpt2Game7Down.text = it.options[1].toString()
            binding.btnOpt3Game7Top.text = it.options[2].toString()
            binding.btnOpt3Game7Down.text = it.options[2].toString()
            binding.btnOpt4Game7Top.text = it.options[3].toString()
            binding.btnOpt4Game7Down.text = it.options[3].toString()
            binding.txtQuestionGame7Top.text = it.question
            binding.txtQuestionGame7Down.text = it.question
        })

        viewModel.answerStatus.observe(viewLifecycleOwner, {
            when (it) {
                AnswerStatus.CORRECT -> {
                    val selectedView = (view.findViewById(valueId.id) as AppCompatButton)
                    selectedView.setBackgroundColor(getColor(R.color.green))
                    if (selectedView.tag == "top") {
                        binding.txtQuestionGame7Top.setTextColor(getColor(R.color.green))
                        binding.txtQuestionGame7Top.text = binding.txtQuestionGame7Top.format(selectedView)
                        binding.txtQuestionGame7Down.text = binding.txtQuestionGame7Top.format(selectedView)
                        topProgress += 100
                        binding.txtCountTop.text = (topProgress / 100).toString()
                        topAnimation()
                        if (topProgress == 1000) {
                            viewModel.setFinished()
                            return@observe
                        }
                    } else {
                        binding.txtQuestionGame7Down.setTextColor(getColor(R.color.green))
                        binding.txtQuestionGame7Down.text = binding.txtQuestionGame7Down.format(selectedView)
                        binding.txtQuestionGame7Top.text = binding.txtQuestionGame7Down.format(selectedView)
                        downProgress += 100
                        binding.txtCountDown.text = (downProgress / 100).toString()
                        downAnimation()
                        if (downProgress == 1000) {
                            viewModel.setFinished()
                            return@observe
                        }
                    }
                    viewModel.loadNextQuestion()
                }

                AnswerStatus.INCORRECT -> {
                    val selectedView = (view.findViewById(valueId.id) as AppCompatButton)
                    selectedView.setBackgroundColor(getColor(R.color.red))
                    if (selectedView.tag == "top") {
                        binding.txtQuestionGame7Top.setTextColor(getColor(R.color.red))
                        binding.txtQuestionGame7Top.text = binding.txtQuestionGame7Top.format(selectedView)
                        topProgress = if (topProgress >= 100) topProgress - 100 else 0
                        binding.txtCountTop.text = (topProgress / 100).toString()
                        topAnimation()
                        for (child in binding.topContainerGame7.children)
                            child.isClickable = false
                    } else {
                        binding.txtQuestionGame7Down.setTextColor(getColor(R.color.red))
                        binding.txtQuestionGame7Down.text = binding.txtQuestionGame7Down.format(selectedView)
                        downProgress = if (downProgress >= 100) downProgress - 100 else 0
                        binding.txtCountDown.text = (downProgress / 100).toString()
                        downAnimation()
                        for (child in binding.downContainerGame7.children)
                            child.isClickable = false
                    }

                    if (!binding.btnOpt1Game7Down.isClickable && !binding.btnOpt1Game7Top.isClickable)
                        viewModel.loadNextQuestion()
                }
            }
        })
        viewModel.gameStatus.observe(viewLifecycleOwner, {
            if (it == GameStatus.FINISHED) {
                val pointTop = binding.txtCountTop.text.toString().toInt()
                val pointDown = binding.txtCountDown.text.toString().toInt()
                val action =
                    GameSevenFragmentDirections.actionGameSevenFragmentToGameSevenResultFragment(pointTop, pointDown)
                findNavController().navigate(action)
            }
        })
    }

    private fun downAnimation() {
        animationDown =
            ObjectAnimator.ofInt(binding.progressGame7Down,
                "progress",
                binding.progressGame7Down.progress,
                downProgress)
        animationDown.duration = ONE_SEC
        animationDown.start()
    }

    private fun topAnimation() {
        animationTop =
            ObjectAnimator.ofInt(binding.progressGame7Top, "progress", binding.progressGame7Top.progress, topProgress)
        animationTop.duration = ONE_SEC
        animationTop.start()
    }

    private fun resetViewStyle() {
        for (child in binding.topContainerGame7.children) {
            if (child is AppCompatButton) {
                child.setBackgroundResource(R.drawable.btn_options_selector)
                child.isClickable = true
            }
        }

        for (child in binding.downContainerGame7.children) {
            if (child is AppCompatButton) {
                child.setBackgroundResource(R.drawable.btn_options_selector)
                child.isClickable = true
            }
        }

        binding.txtQuestionGame7Down.setTextColor(getColor(R.color.blue))
        binding.txtQuestionGame7Top.setTextColor(getColor(R.color.blue))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        valueId = IdValue(view?.id ?: 0, (view as AppCompatButton).text.toString().toInt())
        if (viewModel.questionStatus.value == QuestionStatus.PROCESSING) return
        when (view.id) {
            R.id.btnOpt1Game7Down, R.id.btnOpt2Game7Down, R.id.btnOpt3Game7Down, R.id.btnOpt4Game7Down -> {
                viewModel.checkAnswer(valueId, view.tag.toString())
            }

            R.id.btnOpt1Game7Top, R.id.btnOpt2Game7Top, R.id.btnOpt3Game7Top, R.id.btnOpt4Game7Top -> {
                viewModel.checkAnswer(valueId, view.tag.toString())
            }
        }
    }
}