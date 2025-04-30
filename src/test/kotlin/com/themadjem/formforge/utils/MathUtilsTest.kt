package com.themadjem.formforge.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MathUtilsTest {
    @Test
    fun reverseUnsignedInt16() {
        val expectedValue = 46037u
        val actualValue = MathUtils.ReverseUnsignedInt16(0xABCDu)
        assertEquals(expectedValue,actualValue)
    }

}