package com.example.math.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.math.R
import com.example.math.databinding.FragmentHomeBinding
import com.example.math.utils.*
import com.example.math.view.PolygonalDrwable
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

//        binding.btnGame6.text = "$PLUS $MINUS\n$MULTIPLICATION $DIVISION"
        viewModel.test()

        binding.btnGame1.background = PolygonalDrwable(Color.GREEN, 6)
        binding.btnGame2.background = PolygonalDrwable(Color.GREEN, 6)
        binding.btnGame3.background = PolygonalDrwable(Color.GREEN, 6)
        binding.btnGame4.background = PolygonalDrwable(Color.GREEN, 6)
        binding.btnGame5.background = PolygonalDrwable(Color.MAGENTA, 6)
        binding.btnGame6.background = PolygonalDrwable(Color.MAGENTA, 6)
        binding.btnGame7.background = PolygonalDrwable(Color.MAGENTA, 6)
        binding.btnGame8.background = PolygonalDrwable(Color.YELLOW, 6)
        binding.btnGame9.background = PolygonalDrwable(Color.YELLOW, 6)
        binding.btnGame10.background = PolygonalDrwable(Color.YELLOW, 6)

        binding.btnGame1.setOnClickListener(this)
        binding.btnGame2.setOnClickListener(this)
        binding.btnGame3.setOnClickListener(this)
        binding.btnGame4.setOnClickListener(this)
        binding.btnGame5.setOnClickListener(this)
        binding.btnGame6.setOnClickListener(this)
        binding.btnGame7.setOnClickListener(this)
        binding.btnGame8.setOnClickListener(this)
        binding.btnGame9.setOnClickListener(this)
        binding.btnGame10.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        viewModel.playTapSound()

        when (view?.id) {
            R.id.btnGame1 -> {
                val action = HomeFragmentDirections.actionLevel(GAME_ONE)
                findNavController().navigate(action)
            }
            R.id.btnGame2 -> {
                val action = HomeFragmentDirections.actionLevel(GAME_TWO)
                findNavController().navigate(action)
            }
            R.id.btnGame3 -> {
                val action = HomeFragmentDirections.actionLevel(GAME_THREE)
                findNavController().navigate(action)
            }
            R.id.btnGame4 -> {
                val action = HomeFragmentDirections.actionLevel(GAME_FOUR)
                findNavController().navigate(action)
            }
            R.id.btnGame5 -> {
                val action = HomeFragmentDirections.actionHomeFragmentToChooseLevelFragment()
                findNavController().navigate(action)
            }
            R.id.btnGame6 -> {
                val action = HomeFragmentDirections.actionLevel(GAME_SIX)
                findNavController().navigate(action)
            }
            R.id.btnGame7 -> {
                findNavController().navigate(R.id.action_gameSevenFragment)
            }
            R.id.btnGame8 -> {
                findNavController().navigate(R.id.action_homeFragment_to_gameEightFragment)
            }
            R.id.btnGame9 -> {
                val action = HomeFragmentDirections.actionLevel(GAME_NINE)
                findNavController().navigate(action)
            }
            R.id.btnGame10 -> {
                val action = HomeFragmentDirections.actionLevel(GAME_TEN)
                findNavController().navigate(action)
            }
        }
    }
}