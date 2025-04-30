package com.themadjem.formforge.core.barcode

import com.themadjem.formforge.extensions.toHexString
import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.todo

class IMbBarcodeTest {
    val encoderTestSample1: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00", "310", "987654321", "123456", "12345")
    val encoderTestSample2: IMbBarcode.IMbEncoder =
        IMbBarcode.IMbEncoder("00", "310", "987654321", "123456", "123451234")
    val encoderTestSample3: IMbBarcode.IMbEncoder =
        IMbBarcode.IMbEncoder("00", "310", "987654321", "123456", "12345123412")
    val encoderTestSample4: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00", "310", "987654321", "123456", "")
    val encoderTestSample5: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00", "310", "123456", "123456789", "12345")
    val encoderTestSample6: IMbBarcode.IMbEncoder =
        IMbBarcode.IMbEncoder("00", "310", "123456", "123456789", "123451234")
    val encoderTestSample7: IMbBarcode.IMbEncoder =
        IMbBarcode.IMbEncoder("00", "310", "123456", "123456789", "12345123412")
    val encoderTestSample8: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00", "310", "123456", "123456789", "")

    val specSample1: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("01", "234", "567094", "987654321", "")
    val specSample2: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("01", "234", "567094", "987654321", "01234")
    val specSample3: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("01", "234", "567094", "987654321", "012345678")
    val specSample4: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("01", "234", "567094", "987654321", "01234567891")


    @Test
    fun encodeRoutingNumberTest() {
        val expectedValue5 = (12345 + 1).toBigInteger()
        val expectedValue7 = (123451234 + 100_001).toBigInteger()
        val expectedValue9 = (12345123412 + 1_000_100_001).toBigInteger()
        val expectedValue0 = BigInteger.ZERO

        assertEquals(expectedValue5, encoderTestSample1.encodeRoutingNumber())
        assertEquals(expectedValue5, encoderTestSample5.encodeRoutingNumber())
        assertEquals(expectedValue7, encoderTestSample2.encodeRoutingNumber())
        assertEquals(expectedValue7, encoderTestSample6.encodeRoutingNumber())
        assertEquals(expectedValue9, encoderTestSample3.encodeRoutingNumber())
        assertEquals(expectedValue9, encoderTestSample7.encodeRoutingNumber())
        assertEquals(expectedValue0, encoderTestSample4.encodeRoutingNumber())
        assertEquals(expectedValue0, encoderTestSample8.encodeRoutingNumber())
    }

    @Test
    fun encodeTrackingNumberTest() {
        val expectedValue = "617300310987654321123456"
        val encodedTracking = encoderTestSample1.encodeTrackingNumber().toString()
        assertEquals(expectedValue, encodedTracking)

    }
}


/*For testing only*/
@OptIn(ExperimentalUnsignedTypes::class)
fun main() {
    val specSample1: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder(
        "01",
        "234",
        "567094",
        "987654321",
        "01234567891"
    )
    specSample1.encode()

    TODO("This currently does not work as intended")

    /*
    * binaryData: 16907b2a24abc16a2e5c004b1
    * fcs: 11101010001
    * revFCS: true, false, false, false, true, false, true, false, true, true
    * codewords:0, 787, 607, 1022, 861, 19, 816, 1294, 35, 301
    * mod codewords: 293, 313, 25f, 3fe, 35d, 13, 330, 50e, 23, 25a
    * characters: eab, 85c, 8e4, b06, 6dd, 1740, 17c6, 1200, 123f, 1b2b
    *
    * Expected characters:
    * 0DCB 085C 08E4 0B06 06DD 1740 17C6 1200 123F 1B2B
    * */
}