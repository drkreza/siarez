package com.example.math.ten

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.math.R
import com.example.math.databinding.FragmentGameTenBinding
import com.example.math.enums.GameStatus
import com.example.math.utils.getColor
import com.example.math.utils.setInvisible
import com.example.math.utils.toPx
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameTenFragment : Fragment(R.layout.fragment_game_ten), OnTouchView, View.OnTouchListener {
    private var _binding: FragmentGameTenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameTenViewModel by viewModels()
    private lateinit var drawingView: DrawingView

    private lateinit var leftChildren: Sequence<View>
    private lateinit var rightChildren: Sequence<View>

    private lateinit var startView: TextView
    private lateinit var endView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameTenBinding.bind(view)

        drawingView = binding.drawingView
        drawingView.setOnTouchView(this)

        leftChildren = binding.containerLeftGame10.children
        rightChildren = binding.containerRightGame10.children

        repeat(8) {
            val textView = TextView(requireContext())
            textView.setBackgroundResource(R.drawable.bg_btn_outline_selector)
            textView.setTextColor(getColor(R.color.blue))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
            textView.gravity = Gravity.CENTER
            val params =
                LinearLayout.LayoutParams(toPx(96f), 0, 1f)
            params.setMargins(toPx(8f), toPx(8f), toPx(8f), toPx(8f))
            textView.layoutParams = params
            binding.containerLeftGame10.addView(textView, params)
            textView.setOnTouchListener(this)
        }

        repeat(8) {
            val textView = TextView(requireContext())
            textView.setBackgroundResource(R.drawable.bg_btn_outline_selector)
            textView.setTextColor(getColor(R.color.blue))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
            textView.gravity = Gravity.CENTER
            val params =
                LinearLayout.LayoutParams(toPx(64f), 0, 1f)
            params.setMargins(toPx(8f), toPx(8f), toPx(8f), toPx(8f))
            textView.layoutParams = params
            binding.containerRightGame10.addView(textView, params)
            textView.setOnTouchListener(this)
        }

        viewModel.loadQuestions()
        viewModel.questions.observe(viewLifecycleOwner, {
            val questionTexts = it.keys.toList()

            leftChildren.forEachIndexed { index, child ->
                if (child is TextView) {
                    child.text = questionTexts[index].split("=")[0].trim()
                    child.tag = it[questionTexts[index]]
                }
            }
        })

        viewModel.answers.observe(viewLifecycleOwner, {
            val answerTexts = it.keys.toList()

            rightChildren.forEachIndexed { index, child ->
                if (child is TextView) {
                    child.text = answerTexts[index]
                    child.tag = it[answerTexts[index]]
                }
            }
        })

        viewModel.correctCount.observe(viewLifecycleOwner, {
            if (it == 8) {
                viewModel.addScore()
            }
        })

        viewModel.gameStatus.observe(viewLifecycleOwner, {
            if (it == GameStatus.FINISHED) {
                findNavController().navigateUp()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun touchStart(x: Float, y: Float) {
        if (!drawingView.canDraw)
            return
        for (i in 0 until binding.containerLeftGame10.childCount) {
            val child = binding.containerLeftGame10.getChildAt(i)
            if (child is TextView && (x > 0 && x < (binding.rootGame10.width - child.width) && y > child.top && y < child.bottom)) {
                child.isSelected = true
                startView = child
            }
        }

        for (i in 0 until binding.containerRightGame10.childCount) {
            val child = binding.containerRightGame10.getChildAt(i)
            if (child is TextView && (x > binding.rootGame10.width - child.width && x < binding.rootGame10.width && y > child.top && y < child.bottom)) {
                child.isSelected = true
                startView = child
            }
        }
    }

    override fun touchEnd(x: Float, y: Float) {
        if (!drawingView.canDraw)
            return

        var isFind = false

        for (i in 0 until binding.containerLeftGame10.childCount) {
            val child = binding.containerLeftGame10.getChildAt(i)
            if (child is TextView && (x > 0 && x < (binding.rootGame10.width - child.width) && y > child.top && y < child.bottom)) {

                if(child.text == startView.text){
                    drawingView.resetLine()
                    resetView(startView)
                    return
                }

                isFind = true
                child.isSelected = true
                endView = child

                lifecycleScope.launch {
                    if (startView.tag == endView.tag) {
                        drawingView.isLock = true
                        startView.setBackgroundResource(R.drawable.bg_btn_outline_green)
                        endView.setBackgroundResource(R.drawable.bg_btn_outline_green)
                        drawingView.changeLineColor("#ff2c8731")
                        delay(1000)
                        drawingView.resetLine()
                        startView.setInvisible()
                        endView.setInvisible()
                        viewModel.correctCount.value = viewModel.correctCount.value?.plus(1)
                        drawingView.isLock = false
                    } else {
                        drawingView.isLock = true
                        startView.setBackgroundResource(R.drawable.bg_btn_outline_red)
                        endView.setBackgroundResource(R.drawable.bg_btn_outline_red)
                        drawingView.changeLineColor("#ffe75d67")
                        delay(1000)
                        drawingView.resetLine()
                        resetView(startView)
                        resetView(endView)
                        drawingView.isLock = false
                    }
                }
            }
        }

        for (i in 0 until binding.containerRightGame10.childCount) {
            val child = binding.containerRightGame10.getChildAt(i)
            if (child is TextView && (x > binding.rootGame10.width - child.width && x < binding.rootGame10.width && y > child.top && y < child.bottom)) {
                if(child.text == startView.text){
                    drawingView.resetLine()
                    resetView(startView)
                    return
                }
                isFind = true
                child.isSelected = true
                endView = child

                lifecycleScope.launch {
                    if (startView.tag == endView.tag) {
                        drawingView.isLock = true
                        startView.setBackgroundResource(R.drawable.bg_btn_outline_green)
                        endView.setBackgroundResource(R.drawable.bg_btn_outline_green)
                        drawingView.changeLineColor("#ff2c8731")
                        delay(1000)
                        drawingView.resetLine()
                        startView.setInvisible()
                        endView.setInvisible()
                        viewModel.correctCount.value = viewModel.correctCount.value?.plus(1)
                        drawingView.isLock = false
                    } else {
                        drawingView.isLock = true
                        startView.setBackgroundResource(R.drawable.bg_btn_outline_red)
                        endView.setBackgroundResource(R.drawable.bg_btn_outline_red)
                        drawingView.changeLineColor("#ffe75d67")
                        delay(1000)
                        drawingView.resetLine()
                        resetView(startView)
                        resetView(endView)
                        drawingView.isLock = false
                    }
                }
            }
        }

        if(!isFind){
            drawingView.resetLine()
            resetView(startView)
        }
    }

    private fun resetView(view:View) {
        view.setBackgroundResource(R.drawable.bg_btn_outline_selector)
        view.isSelected = false
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (!drawingView.isLock)
            drawingView.canDraw = true
        return false
    }
}