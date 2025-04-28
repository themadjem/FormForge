package com.themadjem.formforge.utils

import com.themadjem.formforge.extensions.invertByte


object BarcodeUtils {
    fun binaryStringToByteArray(binaryString: String): ByteArray {
        val fullLength = (binaryString.length + 7) / 8 * 8
        val paddedBinary = binaryString.padStart(fullLength, '0')

        return paddedBinary.chunked(8).map { it.toUByte(2).toByte() }.toByteArray()
    }

    fun byteArrayToBinaryString(byteArray: ByteArray): String {
        return byteArray.joinToString(separator = "") {
            String.format("%8s", (it.toInt() and 0xFF).toString(2)).replace(' ', '0')
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val a: ByteArray = byteArrayOf(0b1, 0b10, 0b11, 0b100, 0b101)
        println(byteArrayToBinaryString(a))
        a.invertByte(0)
        println(byteArrayToBinaryString(a))
    }
}