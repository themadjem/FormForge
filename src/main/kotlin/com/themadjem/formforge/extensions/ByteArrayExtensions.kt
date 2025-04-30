package com.themadjem.formforge.extensions

import java.math.BigInteger
import kotlin.experimental.inv

/**
 * Inverts the byte at the given index
 */
fun ByteArray.invertByte(index: Int) {
    require(index in indices) { "Index must be within the bounds of the ByteArray" }
    this[index] = this[index].inv()
}

/**
 * Converts a ByteArray to a BigInteger treating it as unsigned.
 */
fun ByteArray.toUnsignedBigInteger(): BigInteger {
    return BigInteger(1, this)
}

/**
 * Converts a BigInteger to a ByteArray with optional fixed size.
 * If size is specified, it will pad or trim as needed.
 */
fun BigInteger.toFixedSizeByteArray(size: Int? = null): ByteArray {
    var byteArray = this.toByteArray()

    // BigInteger can sometimes add a leading 0 byte if highest bit is set
    if (byteArray.size > 1 && byteArray[0] == 0.toByte()) {
        byteArray = byteArray.copyOfRange(1, byteArray.size)
    }

    if (size != null) {
        if (byteArray.size > size) {
            // Too big, trim from the start
            byteArray = byteArray.copyOfRange(byteArray.size - size, byteArray.size)
        } else if (byteArray.size < size) {
            // Too small, pad with zeros at the start
            val padding = ByteArray(size - byteArray.size) { 0 }
            byteArray = padding + byteArray
        }
    }
    return byteArray
}

/**
 * Gets a single bit from a ByteArray at the given bit index.
 */
fun ByteArray.getBit(bitIndex: Int): Boolean {
    require(bitIndex >= 0) { "Bit index must be non-negative" }
    val byteIndex = bitIndex / 8
    val bitInByte = 7 - (bitIndex % 8)
    require(byteIndex < this.size) { "Bit index out of bounds" }
    return (this[byteIndex].toInt() shr bitInByte) and 1 == 1
}

/**
 * Sets or clears a single bit in a ByteArray at the given bit index.
 */
fun ByteArray.setBit(bitIndex: Int, value: Boolean) {
    require(bitIndex >= 0) { "Bit index must be non-negative" }
    val byteIndex = bitIndex / 8
    val bitInByte = 7 - (bitIndex % 8)
    require(byteIndex < this.size) { "Bit index out of bounds" }
    if (value) {
        this[byteIndex] = (this[byteIndex].toInt() or (1 shl bitInByte)).toByte()
    } else {
        this[byteIndex] = (this[byteIndex].toInt() and (1 shl bitInByte).inv()).toByte()
    }
}

/**
 * Inverts (toggles) a single bit in a ByteArray at the given bit index.
 */
fun ByteArray.invertBit(bitIndex: Int) {
    require(bitIndex >= 0) { "Bit index must be non-negative" }
    val byteIndex = bitIndex / 8
    val bitInByte = 7 - (bitIndex % 8)
    require(byteIndex < this.size) { "Bit index out of bounds" }
    this[byteIndex] = (this[byteIndex].toInt() xor (1 shl bitInByte)).toByte()
}

/**
 * Turns a ByteArray into a nice binary string for debugging.
 * Example: "11001010 00101100 ..."
 */
fun ByteArray.toBinaryString(): String {
    return this.joinToString(" ") { byte ->
        byte.toUByte().toString(2).padStart(8, '0')
    }
}


fun BigInteger.toHexString(): String {
    return toString(16)
}
