package com.themadjem.formforge.utils

object BarcodeUtils {
    fun binaryStringToByteArray(binaryString: String): ByteArray {
        val fullLength = (binaryString.length + 7) / 8 * 8
        val paddedBinary = binaryString.padStart(fullLength, '0')

        return paddedBinary.chunked(8)
            .map { it.toUByte(2).toByte() }
            .toByteArray()
    }
}