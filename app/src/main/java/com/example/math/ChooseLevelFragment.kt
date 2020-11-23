package com.example.math

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.math.databinding.FragmentChooseLevelBinding

class ChooseLevelFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentChooseLevelBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarChooseLevel.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnLevel4.setOnClickListener(this)
        binding.btnLevel6.setOnClickListener(this)
        binding.btnLevel8.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        val level = (view as TextView).text.toString().toInt()
        val action = ChooseLevelFragmentDirections.actionChooseLevelFragmentToGameFiveFragment(level)
        findNavController().navigate(action)
    }
}