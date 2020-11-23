package com.example.math.three

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.fragment.findNavController
import com.example.math.R
import com.example.math.databinding.FragmentGameThreeBinding
import com.example.math.enums.AnswerStatus
import com.example.math.utils.getColor
import com.example.math.utils.toPx

class GameThreeFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentGameThreeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameThreeViewModel by viewModels()
    private lateinit var animation: ObjectAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            binding.container.addView(btn)
            btn.setOnClickListener(this)
            (btn.layoutParams as GridLayout.LayoutParams).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }

        viewModel.questionText.observe(viewLifecycleOwner, {
            binding.txtQuestionThree.setTextColor(getColor(R.color.blue))
            binding.txtQuestionThree.text = it
        })

        viewModel.answerStatus.observe(viewLifecycleOwner, {
            when (it) {
                AnswerStatus.CORRECT -> {
                    binding.txtQuestionThree.setTextColor(getColor(R.color.green))
                    startProgressAnim()
                    viewModel.nextQuestion()
                }

                AnswerStatus.INCORRECT -> {
                    binding.txtQuestionThree.setTextColor(getColor(R.color.red))
                }
            }
        })

        viewModel.isFinished.observe(viewLifecycleOwner, {
            if (it) {
                val action = GameThreeFragmentDirections.actionScoreGameThree(viewModel.getErrors().toTypedArray())
                findNavController().navigate(action)
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
        animation = ObjectAnimator.ofInt(binding.progressGame3, "progress", binding.progressGame3.progress + 50)
        animation.duration = 500
        animation.start()
    }
}