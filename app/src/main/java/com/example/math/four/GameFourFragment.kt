package com.example.math.four

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.gridlayout.widget.GridLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.math.Game
import com.example.math.R
import com.example.math.databinding.FragmentGameFourBinding
import com.example.math.enums.AnswerStatus
import com.example.math.enums.GameStatus
import com.example.math.enums.QuestionStatus
import com.example.math.utils.*

class GameFourFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentGameFourBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameFourViewModel by viewModels()
    private lateinit var upChildren: Sequence<View>
    private lateinit var downChildren: Sequence<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameFourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStartGameFour.setOnClickListener(this)
        upChildren = binding.containerUp.children
        downChildren = binding.containerDown.children

        for (i in 0..11) {
            val txtView = TextView(requireContext())
            txtView.setBackgroundColor(getColor(R.color.white))
            txtView.setTextColor(getColor(R.color.blue))
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            txtView.gravity = Gravity.CENTER
            txtView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
//            txtView.setPadding(toPx(28f), toPx(28f), toPx(28f), toPx(28f))
            val params =
                GridLayout.LayoutParams(ViewGroup.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, toPx(80f)))
            params.setMargins(toPx(2f), toPx(2f), toPx(2f), toPx(2f))
            txtView.layoutParams = params
            binding.containerDown.addView(txtView, params)
            (txtView.layoutParams as GridLayout.LayoutParams).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }

        for (i in 0..11) {
            val txtView = TextView(requireContext())
            txtView.setBackgroundColor(getColor(R.color.blue))
            txtView.gravity = Gravity.CENTER
            //txtView.setPadding(toPx(24f), toPx(24f), toPx(24f), toPx(24f))
            val params =
                GridLayout.LayoutParams(ViewGroup.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, toPx(80f)))
            params.setMargins(toPx(2f), toPx(2f), toPx(2f), toPx(2f))
            txtView.layoutParams = params
            binding.containerUp.addView(txtView, params)
            txtView.setOnClickListener(this)
            (txtView.layoutParams as GridLayout.LayoutParams).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }

        viewModel.loadQuestions()
        viewModel.questions.observe(viewLifecycleOwner, {
            val questionTexts = it.keys.toList()

            downChildren.forEachIndexed { index, child ->
                if (child is TextView) {
                    child.text = questionTexts[index].split("=")[0].trim()
                    child.tag = it[questionTexts[index]]
                }
            }

            upChildren.forEachIndexed { index, child ->
                if (child is TextView) {
                    child.tag = it[questionTexts[index]]
                }
            }
        })

        viewModel.answerStatus.observe(viewLifecycleOwner, {
            when (it.first) {
                AnswerStatus.CORRECT -> {
                    upChildren.forEachIndexed { index, child ->
                        if (it.second.contains(child.tag)) {
                            child.setInvisible()
                            child.isClickable = false
                        }
                    }

                    downChildren.forEachIndexed { index, child ->
                        if (it.second.contains(child.tag)) {
                            child.setInvisible()
                            child.isClickable = false
                        }
                    }

                    viewModel.beginNewQuestion()
                }

                AnswerStatus.INCORRECT -> {
                    upChildren.forEachIndexed { index, child ->
                        if (it.second.contains(child.tag)) {
                            child.animate().alpha(1f).setDuration(300).start()
                        }
                    }
                    if (viewModel.incorrectCount < 5) binding.ratingBar.numStars =
                        5 - (viewModel.incorrectCount) else binding.ratingBar.setInvisible()
                    viewModel.beginNewQuestion()
                }
            }
        })

        viewModel.gameStatus.observe(viewLifecycleOwner, Observer {
            if (it == GameStatus.FINISHED) {
                findNavController().navigateUp()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnStartGameFour -> {
                binding.containerUp.animate().alpha(1f).setDuration(500).start()
                binding.btnStartGameFour.setGone()
                binding.ratingBar.setVisible()
                viewModel.changeGameStatus(GameStatus.STARTED)
            }

            else -> {
                if (viewModel.gameStatus.value != GameStatus.STARTED) return
                if (viewModel.questionStatus == QuestionStatus.PROCESSING) return
                view?.animate()?.alpha(0f)?.setDuration(300)?.start()
                viewModel.checkAnswer(view?.tag as Game)
            }
        }
    }
}