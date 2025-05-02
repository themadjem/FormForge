package com.themadjem.formforge.utils

import kotlin.math.roundToInt

object MathUtils {
    fun ReverseUnsignedInt16(input: UInt): UInt {
        var reverse = 0u;
        var num: UInt = input
        for (index in 0 until 16) {
            reverse = reverse shl 1
            reverse = reverse or (num and 1u)
            num = num shr 1
        }
        return reverse;
    }

    fun inchesToPixels(inches: Float, dpi:Int = 300): Int{
        return (inches * dpi).roundToInt()
    }
}