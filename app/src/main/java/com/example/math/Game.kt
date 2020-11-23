package com.example.math

import android.os.Parcelable
import androidx.annotation.Keep
import com.example.math.enums.Operation
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Game(
    var question: String,
    val options: List<Int>,
    val operation: Operation,
    var answer: Int
) : Parcelable