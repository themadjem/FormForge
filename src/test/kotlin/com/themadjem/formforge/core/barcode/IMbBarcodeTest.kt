package com.themadjem.formforge.core.barcode

import com.themadjem.formforge.extensions.toHexString
import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.test.todo

@ExperimentalUnsignedTypes
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

    @Test
    fun encodeToDAFT1() {
        val payloadData1 = "01234567094987654321"
        val expectedBarcode1 = "ATTFATTDTTADTAATTDTDTATTDAFDDFADFDFTFFFFFTATFAAAATDFFTDAADFTFDTDT"
        val encodedDAFT1 = IMbBarcode().encodeToDAFT(payloadData1)
        assertEquals(expectedBarcode1, encodedDAFT1)
    }

    @Test
    fun encodeToDAFT2() {
        val payloadData2 = "0123456709498765432101234"
        val expectedBarcode2 = "DTTAFADDTTFTDTFTFDTDDADADAFADFATDDFTAAAFDTTADFAAATDFDTDFADDDTDFFT"
        val encodedDAFT2 = IMbBarcode().encodeToDAFT(payloadData2)
        assertEquals(expectedBarcode2, encodedDAFT2)
    }

    @Test
    fun encodeToDAFT3() {
        val payloadData3 = "01234567094987654321012345678"
        val expectedBarcode3 = "ADFTTAFDTTTTFATTADTAAATFTFTATDAAAFDDADATATDTDTTDFDTDATADADTDFFTFA"
        val encodedDAFT3 = IMbBarcode().encodeToDAFT(payloadData3)
        assertEquals(expectedBarcode3, encodedDAFT3)
    }

    @Test
    fun encodeToDAFT4() {
        val payloadData4 = "0123456709498765432101234567891"
        val expectedBarcode4 = "AADTFFDFTDADTAADAATFDTDDAAADDTDTTDAFADADDDTFFFDDTTTADFAAADFTDAADA"
        val encodedDAFT4 = IMbBarcode().encodeToDAFT(payloadData4)
        assertEquals(expectedBarcode4, encodedDAFT4)

    }
}
