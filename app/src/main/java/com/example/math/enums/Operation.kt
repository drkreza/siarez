package com.example.math.enums

import com.example.math.utils.MINUS
import com.example.math.utils.PLUS

enum class Operation(value: String) {
    ADDITION(PLUS),
    SUBTRACTION(MINUS),
    MULTIPLICATION(com.example.math.utils.MULTIPLICATION),
    DIVISION(com.example.math.utils.DIVISION)
}