package com.example.math.level

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.math.R
import com.example.math.databinding.FragmentLevelBinding
import com.example.math.utils.*

class LevelFragment : Fragment(), View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private var _binding: FragmentLevelBinding? = null
    private val binding get() = _binding!!
    private val args: LevelFragmentArgs by navArgs()
    private lateinit var viewModel: LevelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = LevelViewModelFactory(args.type)
        viewModel = ViewModelProvider(this, factory).get(LevelViewModel::class.java)

        binding.toolbarLevel.setOnMenuItemClickListener(this)
        binding.btnPlay.setOnClickListener(this)
        binding.toolbarLevel.setNavigationOnClickListener { findNavController().navigateUp() }

        viewModel.getTotalPoint().observe(viewLifecycleOwner, {
            val animation =
                ObjectAnimator.ofInt(binding.progressBarLevel, "progress", binding.progressBarLevel.progress, it)
            animation.duration = 1000
            animation.start()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnPlay -> {
                when (args.type) {
                    GAME_ONE -> findNavController().navigate(R.id.action_game_one)
                    GAME_TWO -> findNavController().navigate(R.id.action_game_two)
                    GAME_THREE -> findNavController().navigate(R.id.action_game_three)
                    GAME_FOUR -> findNavController().navigate(R.id.action_game_four)
                    GAME_SIX -> findNavController().navigate(R.id.action_game_six)
                    GAME_NINE -> findNavController().navigate(R.id.action_game_nine)
                    GAME_TEN -> findNavController().navigate(R.id.action_game_ten)
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.reset -> {
                val action = LevelFragmentDirections.actionReset(args.type)
                findNavController().navigate(action)
                return true
            }

            else -> return false
        }
    }
}