package com.themadjem.formforge.core.barcode

import com.themadjem.formforge.core.barcode.IMbBarcode.IMbEncoder.BarType
import com.themadjem.formforge.extensions.toFixedSizeByteArray
import com.themadjem.formforge.extensions.toHexString
import com.themadjem.formforge.utils.MathUtils
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.math.BigInteger
import kotlin.UIntArray

@OptIn(ExperimentalUnsignedTypes::class)
class IMbBarcode : Barcode() {
    override fun generate(data: String): RenderedImage {
        val code = IMbEncoder.fromFullCode(data).encode()
        return renderIMb(code)
    }

    private fun stringToBarTypes(bars: String): List<BarType> {
        return bars.map {
            when (it) {
                'F' -> BarType.FULL
                'A' -> BarType.ASCENDER
                'D' -> BarType.DESCENDER
                'T' -> BarType.TRACKER
                else -> BarType.TRACKER
            }
        }.toList()
    }

    fun renderIMb(bars: String): BufferedImage {
        return renderIMb(stringToBarTypes(bars))
    }

    fun renderIMb(barTypes: List<BarType>): BufferedImage {

        val barWidth = 2
        val barHeight = 20
        val image = BufferedImage(barTypes.size * barWidth, barHeight, BufferedImage.TYPE_INT_ARGB)
        val g = image.createGraphics()

        for ((i, bar) in barTypes.withIndex()) {
            val x = i * barWidth
            val (y, height) = when (bar) {
                BarType.FULL -> 0 to barHeight
                BarType.TRACKER -> barHeight / 2 - 1 to 2
                BarType.ASCENDER -> 0 to barHeight / 2
                BarType.DESCENDER -> barHeight / 2 to barHeight / 2
            }

            g.fillRect(x, y, barWidth, height)
        }

        g.dispose()
        TODO("This code generates garbage looking images, this was AI generated, needs work")
        /*
        * Check the spec for barcode properties such as bar width, height, spacing, quiet zones
        * */
        return image
    }

//    fun generateIMbSvg(bars:String): String

    fun generateIMbSvg(barTypes: List<BarType>): String {
        val barWidth = 2
        val barHeight = 20
        val builder = StringBuilder()
        builder.append("<svg width=\"${barTypes.size * barWidth}\" height=\"$barHeight\" xmlns=\"http://www.w3.org/2000/svg\">\n")

        for ((i, bar) in barTypes.withIndex()) {
            val x = i * barWidth
            val (y, height) = when (bar) {
                BarType.FULL -> 0 to barHeight
                BarType.TRACKER -> barHeight / 2 - 1 to 2
                BarType.ASCENDER -> 0 to barHeight / 2
                BarType.DESCENDER -> barHeight / 2 to barHeight / 2
            }

            builder.append("<rect x=\"$x\" y=\"$y\" width=\"$barWidth\" height=\"$height\" fill=\"black\" />\n")
        }

        builder.append("</svg>")
        return builder.toString()
    }



    @ExperimentalUnsignedTypes
    class IMbEncoder(
        val barcodeId: String,
        val serviceTypeId: String,
        val mailerId: String,
        val serialNumber: String,
        val routingNumber: String
    ) {

        companion object {
            fun fromFullCode(code: String): IMbEncoder {
                val barcodeId: String
                val serviceTypeId: String
                val mailerId: String
                val serialNumber: String
                val routingNumber: String

                barcodeId = code.substring(0, 2)
                serviceTypeId = code.substring(2, 5)
                if (code.substring(5, 6) == "9") {
                    mailerId = code.substring(5, 14)
                    serialNumber = if (code.length > 20) code.substring(14, 20) else code.substring(14)
                } else {
                    mailerId = code.substring(5, 11)
                    serialNumber = if (code.length > 20) code.substring(11, 20) else code.substring(11)
                }

                routingNumber = if (code.length > 20) code.substring(20) else ""
                check(barcodeId.length == 2) { "BarcodeID must be 2 digits" }
                check(serviceTypeId.length == 3) { "Service Type ID must be 3 digits" }
                check(mailerId.length == 6 || mailerId.length == 9) { "Mailer ID must be either 6 or 9 digits" }
                check(serialNumber.length == 6 || serialNumber.length == 9) { "Serial Number must be either 9 or 6 digits" }
                val rl = routingNumber.length
                check(rl == 0 || rl == 5 || rl == 9 || rl == 11) { "Routing number must be either 0, 5, 9, or 11 digits" }

                return IMbEncoder(barcodeId, serviceTypeId, mailerId, serialNumber, routingNumber)
            }
        }

        fun encode(): String {
            val binaryData = encodeTrackingNumber()
            val fcs = generateCRC11FrameCheckSequence(binaryData.toFixedSizeByteArray(13))
            val revFCS = (fcs and 1023).toString(2).reversed().toCharArray().map { it.toString().equals("1") }
            val codewords = generateCodewords(binaryData)
//
//            print("binaryData: ")
//            println(binaryData.toHexString())
//            print("fcs: ")
//            println(fcs.toString(2))
//            print("revFCS: ")
//            println(revFCS.joinToString())
//            print("codewords:")
//            println(codewords.joinToString(transform = { it.toString(16).uppercase().padStart(4, '0') }))

            val tab5 = initializeNof13(5u, 1287)
            val tab2 = initializeNof13(2u, 78)
            val bat5 = initializeInvertedTable(tab5)
            val bat2 = initializeInvertedTable(tab2)

            codewords[9] *= 2
            if (fcs.toString(2).toCharArray()[0] == '1') {
                codewords[0] += 659
            }

//            println(
//                "mod codewords: ${
//                    codewords.joinToString(transform = {
//                        it.toString(16).uppercase().padStart(4, '0')
//                    })
//                }"
//            )

            val characters = UIntArray(10)

            for (i in 0 until codewords.size) {
                val cw = codewords[i]
//                println("i=$i; codeword=$cw; FCSbit: ${revFCS[i]}")
                // If the corresponding bit is set in the FCS, pull from the inverted tables
                characters[i] = if (revFCS[i]) {
                    if (cw < 1287) bat5[cw] else bat2[cw - 1287]
                } else {
                    if (cw < 1287) tab5[cw] else tab2[cw - 1287]
                }
            }
//            println(
//                "characters: ${
//                    characters.joinToString(transform = {
//                        it.toString(16).uppercase().padStart(4, '0')
//                    })
//                }"
//            )

            return convertCharactersToBars(characters)
        }


        enum class BarType { FULL, TRACKER, ASCENDER, DESCENDER }

        private data class Bar(var ascender: Boolean, var descender: Boolean) {
            var bar: String
                get() = if (ascender and !descender) "A" else if (!ascender and descender) "D" else if (ascender and descender) "F" else "T"
                set(value) = when (value) {
                    "A" -> {
                        ascender = true
                        descender = false
                    }

                    "D" -> {
                        ascender = false
                        descender = true
                    }

                    "F" -> {
                        ascender = true
                        descender = true
                    }

                    "T" -> {
                        ascender = false
                        descender = false
                    }

                    else -> {}
                }
        }

        private fun getBit(byte: Int, position: Int): Int {
            return (byte shr position) and 1
        }

        fun convertCharactersToBars(characters: UIntArray): String {
            val bars = arrayListOf<Bar>()
            for (i in 0 until 65) {
                val ascender = barTableAsc[i]
                val descender = barTableDes[i]
                val ascBit = getBit(characters[ascender.first].toInt(), ascender.second)
                val desBit = getBit(characters[descender.first].toInt(), descender.second)
                bars.add(Bar(ascBit == 1, desBit == 1))
            }

            return bars.map { it.bar }.toTypedArray().joinToString("")
        }

        private fun generateCodewords(binaryData: BigInteger): IntArray {
            val divisors = intArrayOf(1, 1365, 1365, 1365, 1365, 1365, 1365, 1365, 1365, 636)
            val codewords = IntArray(10)
            var b = binaryData

            for (i in 9 downTo 1) {
                val divisor = divisors[i].toBigInteger()
                codewords[i] = b.remainder(divisor).toInt()
                b = b.divide(divisor)
            }
            codewords[0] = b.toInt()

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

        val barTableAsc = listOf<Pair<Int, Int>>(
            Pair(4, 3),
            Pair(0, 0),
            Pair(2, 8),
            Pair(6, 11),
            Pair(3, 1),
            Pair(5, 12),
            Pair(1, 8),
            Pair(9, 11),
            Pair(8, 10),
            Pair(7, 6),
            Pair(1, 4),
            Pair(2, 12),
            Pair(0, 2),
            Pair(6, 7),
            Pair(4, 9),
            Pair(8, 6),
            Pair(2, 7),
            Pair(9, 9),
            Pair(5, 2),
            Pair(3, 8),
            Pair(0, 4),
            Pair(1, 0),
            Pair(3, 12),
            Pair(7, 7),
            Pair(4, 10),
            Pair(6, 9),
            Pair(8, 0),
            Pair(9, 7),
            Pair(2, 10),
            Pair(0, 5),
            Pair(5, 7),
            Pair(1, 9),
            Pair(9, 6),
            Pair(4, 8),
            Pair(3, 2),
            Pair(8, 12),
            Pair(6, 1),
            Pair(7, 4),
            Pair(1, 2),
            Pair(2, 0),
            Pair(4, 1),
            Pair(3, 5),
            Pair(9, 4),
            Pair(5, 6),
            Pair(7, 12),
            Pair(8, 1),
            Pair(3, 0),
            Pair(0, 9),
            Pair(2, 4),
            Pair(1, 7),
            Pair(4, 5),
            Pair(0, 10),
            Pair(9, 2),
            Pair(1, 6),
            Pair(7, 9),
            Pair(0, 11),
            Pair(2, 2),
            Pair(4, 12),
            Pair(6, 6),
            Pair(3, 7),
            Pair(7, 5),
            Pair(1, 11),
            Pair(9, 0),
            Pair(5, 3),
            Pair(8, 2)
        )

        val barTableDes = listOf<Pair<Int, Int>>(
            Pair(7, 2),
            Pair(1, 10),
            Pair(9, 12),
            Pair(5, 5),
            Pair(8, 9),
            Pair(0, 1),
            Pair(2, 5),
            Pair(4, 4),
            Pair(6, 3),
            Pair(3, 9),
            Pair(5, 11),
            Pair(8, 5),
            Pair(9, 10),
            Pair(7, 1),
            Pair(3, 6),
            Pair(0, 3),
            Pair(6, 4),
            Pair(1, 1),
            Pair(7, 10),
            Pair(4, 0),
            Pair(6, 2),
            Pair(8, 11),
            Pair(9, 8),
            Pair(2, 6),
            Pair(5, 1),
            Pair(1, 12),
            Pair(7, 3),
            Pair(5, 8),
            Pair(4, 6),
            Pair(3, 4),
            Pair(8, 4),
            Pair(7, 11),
            Pair(6, 0),
            Pair(0, 6),
            Pair(2, 1),
            Pair(5, 9),
            Pair(4, 11),
            Pair(9, 5),
            Pair(3, 3),
            Pair(0, 7),
            Pair(1, 3),
            Pair(6, 10),
            Pair(8, 7),
            Pair(2, 11),
            Pair(0, 8),
            Pair(4, 2),
            Pair(5, 10),
            Pair(9, 3),
            Pair(6, 5),
            Pair(7, 8),
            Pair(5, 0),
            Pair(2, 3),
            Pair(6, 12),
            Pair(3, 11),
            Pair(8, 8),
            Pair(5, 4),
            Pair(1, 5),
            Pair(9, 1),
            Pair(8, 3),
            Pair(7, 0),
            Pair(4, 7),
            Pair(0, 12),
            Pair(2, 9),
            Pair(6, 8),
            Pair(3, 10)
        )

    }


}