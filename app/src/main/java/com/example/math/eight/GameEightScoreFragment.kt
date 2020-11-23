package com.example.math.eight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.math.databinding.FragmentGameEightScoreBinding

class GameEightScoreFragment : Fragment() {
    private var _binding: FragmentGameEightScoreBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameEightScoreViewModel by viewModels()
    private val args: GameEightScoreFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameEightScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentScore = args.score
        val type = args.type
        val bestScore = viewModel.getBestScore(type, currentScore)

        binding.txtBestScore8.text = "Best Time: $bestScore"
        binding.txtCurrentScore8.text = "Time: $currentScore"

        binding.btnClosScore8.setOnClickListener { findNavController().navigateUp() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}