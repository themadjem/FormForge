package com.themadjem.formforge.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BarcodeUtilsTest {
    private val testSample0: String = "0"
    private val testSample1: String = "10"
    private val testSample2: String = "10001"

    @Test
    fun testSample0(){
        val expectedValue = byteArrayOf(0)
        val actualValue = BarcodeUtils.binaryStringToByteArray(testSample0)
        assertArrayEquals(expectedValue, actualValue)
    }

    @Test
    fun testSample1(){
        val expectedValue = byteArrayOf(0b10)
        val actualValue = BarcodeUtils.binaryStringToByteArray(testSample1)
        assertArrayEquals(expectedValue, actualValue)
    }

    @Test
    fun testSample2(){
        val expectedValue = byteArrayOf(0b10001)
        val actualValue = BarcodeUtils.binaryStringToByteArray(testSample2)
        assertArrayEquals(expectedValue, actualValue)
    }
}