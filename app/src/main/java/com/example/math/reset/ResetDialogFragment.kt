package com.example.math.reset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.math.R
import com.example.math.databinding.FragmentResetDialogBinding
import com.example.math.utils.setParams

class ResetDialogFragment : DialogFragment(), View.OnClickListener {
    private var _binding: FragmentResetDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ResetViewModel by viewModels()
    private val args: ResetDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetDialogBinding.inflate(inflater, container, false)
        hideStatusBar()
        return binding.root
    }

    private fun hideStatusBar() {
        val decorView = dialog?.window?.decorView
        decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancelDialog.setOnClickListener(this)
        binding.btnResetDialog.setOnClickListener(this)

        viewModel.isReset.observe(viewLifecycleOwner, Observer {
            if (it) {
                val action = ResetDialogFragmentDirections.actionResetDialogFragmentToLevelFragment2(args.type)
                findNavController().navigate(action)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnCancelDialog -> findNavController().navigateUp()
            R.id.btnResetDialog -> viewModel.resetProgress(args.type)
        }
    }

    override fun onStart() {
        super.onStart()
        setParams()
    }
}