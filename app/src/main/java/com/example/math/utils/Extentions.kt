package com.example.math.utils

import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.lang.StringBuilder


fun TextView.format(button: Button?): String {
    val questionText = text.toString().split("=")[0].trim()
    return "$questionText = ${button?.text}"
}

fun String.attach(value: String?): String {
    val parts = split("=")
    val partOne = parts[0].trim()
    val partTwo = parts[1].trim()
    if ((partTwo == "?" && value == "0") || partTwo.length == 3) return this
    return if (partTwo == "?")
        "$partOne = $value"
    else
        StringBuilder().append(this).append(value).toString()
}

fun String.detach(): String {
    val parts = split("=")
    val partOne = parts[0].trim()
    val partTwo = parts[1].trim()
    if (partTwo == "?") return this
    return if (partTwo.length == 1)
        "$partOne = ?"
    else
        substring(0, length - 1)
}

fun String.getSelectedAnswer(): Int {
    return split("=")[1].trim().toInt()
}

fun Fragment.getColor(@ColorRes id: Int) = ContextCompat.getColor(requireContext(), id)

fun Int.digitCount() = toString().length

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun Fragment.toPx(dp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
}

fun Fragment.screenWidth(): Int {
    val displayMetrics = DisplayMetrics()
    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun DialogFragment.setParams() {
    val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
    params.width = (screenWidth() * 0.9).toInt()
    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
    dialog!!.window!!.attributes = params as WindowManager.LayoutParams
}