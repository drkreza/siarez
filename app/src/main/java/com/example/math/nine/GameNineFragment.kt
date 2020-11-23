package com.example.math.nine

import android.content.ClipData
import android.os.Bundle
import android.util.TypedValue
import android.view.DragEvent
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.math.CustomDragShadowBuilder
import com.example.math.R
import com.example.math.databinding.FragmentGameNineBinding
import com.example.math.enums.Operation
import com.example.math.utils.*

class GameNineFragment : Fragment(R.layout.fragment_game_nine), View.OnTouchListener, View.OnDragListener, View.OnClickListener {
    private var _binding: FragmentGameNineBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameNineViewModel by viewModels()
    private var lastTouchedView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameNineBinding.bind(view)
        binding.btnHelp.setOnClickListener(this)

        val numbers = viewModel.getAnswers()

        repeat(5) {
            val linearLayout = LinearLayout(requireContext())
            linearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            val game = viewModel.games.value?.get(it)
            repeat(5) {
                val frameLayout = FrameLayout(requireContext())
                val frameParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    toPx(60f), 1f)
                frameParams.setMargins(toPx(4f), toPx(4f), toPx(4f), toPx(4f))
                frameLayout.layoutParams = frameParams
                frameLayout.setBackgroundResource(if (it == 0 || it == 2) R.drawable.bg_drag_selector else R.color.white)
                if (it != 0 && it != 2) {
                    val textView = TextView(requireContext())
                    val textParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT)
                    textView.layoutParams = textParams
                    textView.text = if (it == 1) {
                        when (game?.operation) {
                            Operation.ADDITION -> PLUS
                            Operation.SUBTRACTION -> MINUS
                            Operation.DIVISION -> DIVISION
                            else -> MULTIPLICATION
                        }
                    } else if (it == 3) "=" else game?.answer.toString()
                    textView.gravity = Gravity.CENTER
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
                    textView.setTextColor(getColor(R.color.blue))
                    frameLayout.addView(textView)
                }
                if (it == 0 || it == 2) {
                    frameLayout.setOnDragListener(this)
                }
                linearLayout.addView(frameLayout)
            }
            binding.topContainer.addView(linearLayout)
        }

        repeat(2) { index ->
            val linearLayout = LinearLayout(requireContext())
            linearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            repeat(5) {
                val frameLayout = FrameLayout(requireContext())
                val frameParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    toPx(60f), 1f)
                frameParams.setMargins(toPx(4f), toPx(4f), toPx(4f), toPx(4f))
                frameLayout.layoutParams = frameParams
                frameLayout.setBackgroundResource(R.drawable.bg_drag_selector)
                frameLayout.setOnDragListener(this)
                frameLayout.tag = numbers[(index * 5) + it]
                val textView = createTextView(numbers[(index * 5) + it])
                frameLayout.addView(textView)
                linearLayout.addView(frameLayout)
            }

            binding.bottomContainer.addView(linearLayout)
        }
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(requireContext())
        val textParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        textView.layoutParams = textParams
        textView.setBackgroundResource(R.color.blue)
        textView.setTextColor(getColor(R.color.white))
        textView.text = text
        textView.tag = text
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
        textView.gravity = Gravity.CENTER
        textView.setOnTouchListener(this)
        return textView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        lastTouchedView = view
        val tag = view?.tag.toString()

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val clipData = ClipData.newPlainText("", tag)
                val shadow = CustomDragShadowBuilder(view!!)
                view?.startDrag(clipData, shadow, view, 0)
                return true
            }

            MotionEvent.ACTION_UP -> {
                view?.performClick()
                return true
            }

            else -> return false
        }
    }

    override fun onDrag(view: View?, event: DragEvent?): Boolean {
        when (event?.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                return true
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                view?.isSelected = true
                return true
            }

            DragEvent.ACTION_DROP -> {
                if ((view as FrameLayout).childCount > 0) {
                    val lastText = (lastTouchedView as TextView).text
                    val newText = (view.getChildAt(0) as TextView).text
                    val lastParent = ((lastTouchedView as TextView).parent as FrameLayout)
                    lastParent.removeView(lastTouchedView)
                    lastParent.addView(createTextView(newText.toString()))
                    view.removeViewAt(0)
                    view.addView(createTextView(lastText.toString()))

                    styleViews()
                    return false
                } else {
                    val clipData = event.clipData
                    val data = clipData.getItemAt(0).text.toString()
                    val textView = createTextView(data)
                    view.addView(textView)
                    ((lastTouchedView?.parent) as FrameLayout).removeView(lastTouchedView)

                    styleViews()
                    return true
                }
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                view?.isSelected = false
                return true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                if (event.result) {
                    lastTouchedView?.setInvisible()
                }

                return true
            }

            else -> return false
        }
    }

    private fun updateTextColors(
        textViewOne: TextView?,
        textViewTwo: TextView,
        textViewThree: TextView?,
        textViewFour: TextView,
        textViewFive: TextView,
        @ColorRes colour: Int,
        isInEditMode: Boolean = false
    ) {
        textViewOne?.setBackgroundColor(if (isInEditMode) getColor(R.color.blue) else getColor(colour))
        textViewTwo.setTextColor(getColor(colour))
        textViewThree?.setBackgroundColor(if (isInEditMode) getColor(R.color.blue) else getColor(colour))
        textViewFour.setTextColor(getColor(colour))
        textViewFive.setTextColor(getColor(colour))
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnHelp -> {
                val action = GameNineFragmentDirections.actionGameNineFragmentToGameNineHelpFragment(viewModel.games.value!!.toTypedArray())
                findNavController().navigate(action)
            }
        }
    }

    fun styleViews(){
        var complete = true

        for (layout in binding.topContainer.children) {
            if (layout !is LinearLayout)
                continue

            val textViewOne = (layout[0] as FrameLayout).getChildAt(0) as TextView?
            val textViewTwo = (layout[1] as FrameLayout).getChildAt(0) as TextView
            val textViewThree = (layout[2] as FrameLayout).getChildAt(0) as TextView?
            val textViewFour = (layout[3] as FrameLayout).getChildAt(0) as TextView
            val textViewFive = (layout[4] as FrameLayout).getChildAt(0) as TextView

            val numberOne = textViewOne?.text.toString().toIntOrNull()
            val numberTwo = textViewThree?.text.toString().toIntOrNull()
            val operator = textViewTwo.text
            val result = textViewFive.text.toString().toInt()
            when (operator) {
                PLUS -> {
                    if (numberOne == null || numberTwo == null) {
                        updateTextColors(textViewOne, textViewTwo, textViewThree,
                            textViewFour, textViewFive, R.color.blue, true)
                        complete = false
                    } else if (numberOne + numberTwo != result) {
                        updateTextColors(textViewOne, textViewTwo, textViewThree, textViewFour, textViewFive, R.color.red)
                        complete = false
                    } else {
                        updateTextColors(textViewOne, textViewTwo, textViewThree, textViewFour, textViewFive, R.color.green)
                    }
                }

                MINUS -> {
                    if (numberOne == null || numberTwo == null) {
                        updateTextColors(textViewOne, textViewTwo, textViewThree,
                            textViewFour, textViewFive, R.color.blue, true)
                        complete = false
                    } else if (numberOne - numberTwo != result) {
                        updateTextColors(textViewOne, textViewTwo, textViewThree, textViewFour, textViewFive, R.color.red)
                        complete = false
                    } else {
                        updateTextColors(textViewOne, textViewTwo, textViewThree, textViewFour, textViewFive, R.color.green)
                    }
                }

                DIVISION -> {
                    if (numberOne == null || numberTwo == null) {
                        updateTextColors(textViewOne, textViewTwo, textViewThree,
                            textViewFour, textViewFive, R.color.blue, true)
                        complete = false
                    } else if (numberOne / numberTwo != result) {
                        updateTextColors(textViewOne, textViewTwo, textViewThree, textViewFour, textViewFive, R.color.red)
                        complete = false
                    } else {
                        updateTextColors(textViewOne, textViewTwo, textViewThree, textViewFour, textViewFive, R.color.green)
                    }
                }

                MULTIPLICATION -> {
                    if (numberOne == null || numberTwo == null) {
                        updateTextColors(textViewOne, textViewTwo, textViewThree,
                            textViewFour, textViewFive, R.color.blue, true)
                        complete = false
                    } else if (numberOne * numberTwo != result) {
                        updateTextColors(textViewOne, textViewTwo, textViewThree, textViewFour, textViewFive, R.color.red)
                        complete = false
                    } else {
                        updateTextColors(textViewOne, textViewTwo, textViewThree, textViewFour, textViewFive, R.color.green)
                    }
                }
            }
        }

        if (complete) {
            PrefManager.addTotalScore(TOTAL_SCORE_GAME_NINE, 40)
            findNavController().navigateUp()
        }
    }
}