package com.example.math.nine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.math.R
import com.example.math.databinding.FragmentGameNineHelpBinding
import com.example.math.utils.attach
import java.lang.StringBuilder

class GameNineHelpFragment : Fragment(R.layout.fragment_game_nine_help) {
    private var _binding: FragmentGameNineHelpBinding? = null
    private val binding get() = _binding!!
    private val args:GameNineHelpFragmentArgs by navArgs()
    private val stringBuilder = StringBuilder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameNineHelpBinding.bind(view)
        binding.btnCloseHelp.setOnClickListener { findNavController().navigateUp() }
        stringBuilder.append("Move the numbers to get the right equality\n\n")
        val games = args.games
        for(game in games){
            val result = game.question.attach(game.answer.toString())
            stringBuilder.append("$result\n")
        }
        binding.txtHints.text = stringBuilder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}