package com.themadjem.formforge.core.barcode

import com.themadjem.formforge.utils.BarcodeUtils
import java.awt.Image
import java.math.BigInteger

class IMbBarcode : Barcode() {
    override fun generate(data: String): Image {
        TODO("Not yet implemented")
    }

    class IMbEncoder(
        val barcodeId: String,
        val serviceTypeId: String,
        val mailerId: String,
        val serialNumber: String,
        val routingNumber: String
    ) {
        val barcodeIdBytes = BarcodeUtils.binaryStringToByteArray(barcodeId)
        val serviceTypeIdBytes = BarcodeUtils.binaryStringToByteArray(serviceTypeId)
        val mailerIdBytes = BarcodeUtils.binaryStringToByteArray(mailerId)
        val serialNumberBytes = BarcodeUtils.binaryStringToByteArray(serialNumber)

        fun encode(): ByteArray {
            val routingNumberLength = routingNumber.length
            val routingEncoded: String = encodeRoutingNumber()
            return BarcodeUtils.binaryStringToByteArray("0") //placeholder
        }

        fun encodeRoutingNumber(): String {
            val routingLen = routingNumber.length
            when (routingLen) {
                0 -> {
                    return "0"
                }

                5 -> {
                    return routingNumber.toBigInteger().add(BigInteger.ONE).toString()
                }

                9 -> {
                    return routingNumber.toBigInteger().add(BigInteger.valueOf(100_001)).toString()
                }

                11 -> {
                    return routingNumber.toBigInteger().add(BigInteger.valueOf(1_000_100_001)).toString()
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


    }


}

