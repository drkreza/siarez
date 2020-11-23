package com.example.math.five

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.math.IdValue
import com.example.math.R
import com.example.math.databinding.FragmentGameFiveBinding
import com.example.math.enums.AnswerStatus
import com.example.math.enums.GameStatus
import com.example.math.utils.*

class GameFiveFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentGameFiveBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GameFiveViewModel
    private val args: GameFiveFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameFiveBinding.inflate(inflater, container, false)
        val level = args.level
        val factory = GameFiveViewModelFactory(level)
        viewModel = ViewModelProvider(this, factory).get(GameFiveViewModel::class.java)

        binding.gridNumbers.rowCount = level
        binding.gridNumbers.columnCount = level

        for (i in 1..level * level) {
            val txtView = TextView(requireContext())
            txtView.setBackgroundResource(R.drawable.btn_game5_selector)
            txtView.setTextColor(getColor(R.color.white))
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, if (level == 4) 44f else if (level == 6) 30f else 24f)
            txtView.gravity = Gravity.CENTER
//            txtView.setPadding(toPx(12f), toPx(12f), toPx(12f), toPx(12f))
            val params =
                GridLayout.LayoutParams(ViewGroup.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT,
                    if (level == 4) toPx(80f) else if (level == 4) toPx(56f) else toPx(40f)))
            params.setMargins(toPx(2f), toPx(2f), toPx(2f), toPx(2f))
            txtView.layoutParams = params
            binding.gridNumbers.addView(txtView, params)
            (txtView.layoutParams as GridLayout.LayoutParams).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            txtView.id = ViewCompat.generateViewId()
            txtView.setOnClickListener(this)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.numbers.observe(viewLifecycleOwner, {
            for ((index, child) in binding.gridNumbers.children.withIndex()) {
                (child as TextView).text = it[index].toString()
                child.tag = IdValue(child.id, child.text.toString().toInt())
            }
            viewModel.loadQuestion()
        })

        viewModel.time.observe(viewLifecycleOwner, {
            binding.txtTimer5.text = it.toString()
        })

        viewModel.question.observe(viewLifecycleOwner, {
            binding.txtAnswer5.text = it.toString()
        })

        viewModel.answerStatus.observe(viewLifecycleOwner, {
            if (it == AnswerStatus.CORRECT) {
                for ((index, child) in binding.gridNumbers.children.withIndex()) {
                    if (viewModel.getSelectedAns().contains(child.tag)) {
                        child.setInvisible()
                        child.isClickable = false
                    }
                }
                viewModel.loadQuestion()
            }
        })

        viewModel.gameStatus.observe(viewLifecycleOwner, {
            val type = when (args.level) {
                4 -> BEST_SCORE_GAME_FIVE_4
                6 -> BEST_SCORE_GAME_FIVE_6
                else -> BEST_SCORE_GAME_FIVE_8
            }
            val action =
                GameFiveFragmentDirections.actionGameFiveFragmentToGameEightScoreFragment(binding.txtTimer5.text.toString()
                    .toInt(),
                    type)
            findNavController().navigate(action)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        view?.let {
            it.isSelected = !it.isSelected
            viewModel.chooseOptions(it.tag as IdValue)
        }
    }
}