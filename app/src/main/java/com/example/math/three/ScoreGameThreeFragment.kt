package com.example.math.three

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.math.R
import com.example.math.databinding.FragmentScoreGameThreeBinding
import com.example.math.utils.attach
import com.example.math.utils.getColor
import com.example.math.utils.toPx

class ScoreGameThreeFragment : Fragment() {
    private var _binding: FragmentScoreGameThreeBinding? = null
    private val binding get() = _binding!!
    private val args: ScoreGameThreeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScoreGameThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val errors = args.games
        binding.rateBaThree.numStars = if (errors.size < 5) 5 - errors.size else 1
        for (error in errors) {
            val txtError = TextView(requireContext())
            txtError.text = error.question.attach(error.answer.toString())
            txtError.setTextColor(getColor(R.color.blue))
            txtError.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
            val params =
                LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            params.setMargins(0,0,0,toPx(8f))
            txtError.layoutParams = params
            binding.containerErrorsThree.addView(txtError)
        }

        binding.btnSubmitScoreThree.setOnClickListener { findNavController().navigateUp() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}