package com.example.math.eight

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.gridlayout.widget.GridLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.math.R
import com.example.math.databinding.FragmentGameEightBinding
import com.example.math.enums.AnswerStatus
import com.example.math.utils.BEST_SCORE_GAME_EIGHT
import com.example.math.utils.getColor
import com.example.math.utils.toPx

class GameEightFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentGameEightBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameEightViewModel by viewModels()
    private lateinit var animation: ObjectAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameEightBinding.inflate(inflater, container, false)
        repeat(12) {
            val btn = Button(requireContext())
            btn.text = if (it == 9) "\u232B" else if (it == 10) "0" else if (it == 11) "\u21B2" else (it + 1).toString()
            btn.tag = if (it == 10) 0 else it + 1
            btn.setBackgroundResource(R.drawable.btn_options_selector)
            btn.setTextColor(getColor(android.R.color.white))
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
            btn.setPadding(toPx(12f), toPx(12f), toPx(12f), toPx(12f))
            val params = LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT)
            params.setMargins(if ((it + 1) % 3 == 1) toPx(4f) else 0, 0, toPx(4f), toPx(4f))
            btn.layoutParams = params
            binding.containerGame8.addView(btn)
            btn.setOnClickListener(this)
            (btn.layoutParams as GridLayout.LayoutParams).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.time.observe(viewLifecycleOwner, {
            binding.txtTimerGame8.text = it.toString()
        })

        viewModel.questionText.observe(viewLifecycleOwner, {
            binding.txtAnswerGame8.setTextColor(getColor(R.color.blue))
            binding.txtQuestionGame8.text = it
        })

        viewModel.answerText.observe(viewLifecycleOwner, {
            binding.txtAnswerGame8.text = it
        })

        viewModel.gameStatus.observe(viewLifecycleOwner, {
            val action =
                GameEightFragmentDirections.actionGame8Score(binding.txtTimerGame8.text.toString().toInt(), BEST_SCORE_GAME_EIGHT)
            findNavController().navigate(action)
        })

        viewModel.answerStatus.observe(viewLifecycleOwner, {
            when (it) {
                AnswerStatus.CORRECT -> {
                    binding.txtAnswerGame8.setTextColor(getColor(R.color.green))
                    startProgressAnim()
                    viewModel.loadNextQuestion()
                }

                AnswerStatus.INCORRECT -> {
                    binding.txtAnswerGame8.setTextColor(getColor(R.color.red))
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.tag) {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> viewModel.attach(view.tag.toString())
            10 -> viewModel.detach()
            12 -> viewModel.checkAnswer()
        }
    }

    private fun startProgressAnim() {
        animation = ObjectAnimator.ofInt(binding.progressGame8, "progress", binding.progressGame8.progress + 200)
        animation.duration = 500
        animation.start()
    }
}