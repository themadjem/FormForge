package com.themadjem.formforge.core.barcode

import com.themadjem.formforge.extensions.toFixedSizeByteArray
import com.themadjem.formforge.extensions.toHexString
import com.themadjem.formforge.utils.MathUtils
import java.awt.Image
import java.math.BigInteger
import kotlin.UIntArray

class IMbBarcode : Barcode() {
    override fun generate(data: String): Image {
        TODO("Not yet implemented")
    }

    @ExperimentalUnsignedTypes
    class IMbEncoder(
        val barcodeId: String,
        val serviceTypeId: String,
        val mailerId: String,
        val serialNumber: String,
        val routingNumber: String
    ) {

        fun encode() {
            val binaryData = encodeTrackingNumber()
            val fcs = generateCRC11FrameCheckSequence(binaryData.toFixedSizeByteArray(13))
            val revFCS = (fcs and 1023).toString(2).reversed().toCharArray().map { it.toString().equals("1") }
            val codewords = generateCodewords(binaryData)

            print("binaryData: ")
            println(binaryData.toHexString())
            print("fcs: ")
            println(fcs.toString(2))
            print("revFCS: ")
            println(revFCS.joinToString())
            print("codewords:")
            println(codewords.joinToString())

            val tab5 = initializeNof13(5u, 1287)
            val tab2 = initializeNof13(2u, 78)
            val bat5 = initializeInvertedTable(tab5)
            val bat2 = initializeInvertedTable(tab2)

            codewords[9] *= 2
            if (fcs.toString(2).toCharArray()[0] == '1') {
                codewords[0] += 659
            }

            println("mod codewords: ${codewords.joinToString(transform = {it.toString(16)})}")

            val characters = UIntArray(10)

            for (i in 0 until codewords.size) {
                val cw = codewords[i]

                // If the corresponding bit is set in the FCS, pull from the inverted tables
                characters[i] = if (revFCS[i]) {
                    if (cw < 1287) bat5[cw] else bat2[cw - 1287]
                } else {
                    if (cw < 1287) tab5[cw] else tab2[cw - 1287]
                }
            }
            println("characters: ${characters.joinToString(transform = {it.toString(16)})}")


        }

        private fun generateCodewords(binaryData: BigInteger): IntArray {
            val divisors = intArrayOf(1, 1365, 1365, 1365, 1365, 1365, 1365, 1365, 1365, 636)
            val codewords = IntArray(10)
            var b = binaryData

            for (i in 9 downTo 0) {
                val divisor = divisors[i].toBigInteger()
                codewords[i] = b.remainder(divisor).toInt()
                b = b.divide(divisor)
            }

            return codewords
        }

        fun encodeTrackingNumber(): BigInteger {
            val tracking: String = "$barcodeId$serviceTypeId$mailerId$serialNumber"
            require(tracking.length == 20) { "Tracking code must be 20 digits" }
            var binaryData: BigInteger = encodeRoutingNumber()
            val bid0: String = barcodeId.toCharArray()[0].toString()
            val bid1: String = barcodeId.toCharArray()[1].toString()
            binaryData = binaryData
                .multiply(10.toBigInteger())
                .add(bid0.toBigInteger())
                .multiply(5.toBigInteger())
                .add(bid1.toBigInteger())
            for (t in tracking.substring(2)) {
                binaryData = binaryData.multiply(BigInteger.TEN).add(t.toString().toBigInteger())
            }

            return binaryData
        }

        fun encodeRoutingNumber(): BigInteger {
            val routingLen = routingNumber.length
            when (routingLen) {
                0 -> {
                    return BigInteger.ZERO
                }

                5 -> {
                    return routingNumber.toBigInteger().add(BigInteger.ONE)
                }

                9 -> {
                    return routingNumber.toBigInteger().add(BigInteger.valueOf(100_001))
                }

                11 -> {
                    return routingNumber.toBigInteger().add(BigInteger.valueOf(1_000_100_001))
                }

                else -> {
                    error("Routing Number must be 0, 5, 9, or 11 digits")
                }
            }

        }

        fun generateCRC11FrameCheckSequence(byteArray: ByteArray): Int {
            val generatorPolynomial = 0x0F35
            var frameCheckSequence = 0x07FF
            var data: Int


            var byteIndex: Int
            var bit: Int

            var pointer = 0

            // Do most significant byte skipping the 2 most significant bits
            data = (byteArray[pointer].toInt() and 0xFF) shl 5
            pointer++

            for (bit in 2 until 8) {
                if ((frameCheckSequence xor data) and 0x400 != 0) {
                    frameCheckSequence = (frameCheckSequence shl 1) xor generatorPolynomial
                } else {
                    frameCheckSequence = frameCheckSequence shl 1
                }
                frameCheckSequence = frameCheckSequence and 0x7FF
                data = data shl 1
            }

            // Do rest of the bytes
            for (byteIndex in 1 until 13) {
                data = (byteArray[pointer].toInt() and 0xFF) shl 3
                pointer++
                for (bit in 0 until 8) {
                    if ((frameCheckSequence xor data) and 0x400 != 0) {
                        frameCheckSequence = (frameCheckSequence shl 1) xor generatorPolynomial
                    } else {
                        frameCheckSequence = frameCheckSequence shl 1
                    }
                    frameCheckSequence = frameCheckSequence and 0x7FF
                    data = data shl 1
                }
            }

            return frameCheckSequence
        }

        @ExperimentalUnsignedTypes
        fun initializeNof13(n: UInt, tableLength: Int): UIntArray {
            val table = UIntArray(tableLength.toInt())
            var lowerIndex: Int = 0
            var upperIndex: Int = tableLength - 1


            for (count in 0u until 8192u) {

                /* If we don't have the right number of bits on, go on to the next value */
                if (count.countOneBits() != n.toInt()) continue
                /* If the reverse is less than count, we have already visited this pair before */
                val reverse = MathUtils.ReverseUnsignedInt16(count) shr 3
                if (reverse < count) continue

                /* If Count is symmetric, place it at the first free slot from the end of the */
                /* list. Otherwise, place it at the first free slot from the beginning of the */
                /* list AND place Reverse at the next free slot from the beginning of the list.*/

                if (count == reverse) {
                    table[upperIndex] = count
                    upperIndex--
                } else {
                    table[lowerIndex] = count
                    lowerIndex++
                    table[lowerIndex] = reverse
                    lowerIndex++
                }

            }
            /* Make sure the lower and upper parts of the table meet properly */
            check(lowerIndex == (upperIndex + 1)) { "Table could not be generated: lowerIndex = $lowerIndex; upperIndex = $upperIndex" }

            return table
        }

        fun initializeInvertedTable(table: UIntArray): UIntArray {
            return table.map {
                it.inv() and 8191u
            }.toUIntArray()
        }
    }


}