package com.themadjem.formforge.core.barcode

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class IMbBarcodeTest {
    val encoderTestSample1: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00","310","987654321","123456","12345")
    val encoderTestSample2: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00","310","987654321","123456","123451234")
    val encoderTestSample3: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00","310","987654321","123456","12345123412")
    val encoderTestSample4: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00","310","987654321","123456","")
    val encoderTestSample5: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00","310","123456","123456789","12345")
    val encoderTestSample6: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00","310","123456","123456789","123451234")
    val encoderTestSample7: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00","310","123456","123456789","12345123412")
    val encoderTestSample8: IMbBarcode.IMbEncoder = IMbBarcode.IMbEncoder("00","310","123456","123456789","")

    @Test
    fun EncodeRoutingNumberTest(){
        val expectedValue5 = (12345 + 1).toString()
        val expectedValue7 = (123451234 + 100_001).toString()
        val expectedValue9 = (12345123412 + 1_000_100_001).toString()
        val expectedValue0 = "0"

        assertEquals(expectedValue5,encoderTestSample1.encodeRoutingNumber())
        assertEquals(expectedValue5,encoderTestSample5.encodeRoutingNumber())
        assertEquals(expectedValue7,encoderTestSample2.encodeRoutingNumber())
        assertEquals(expectedValue7,encoderTestSample6.encodeRoutingNumber())
        assertEquals(expectedValue9,encoderTestSample3.encodeRoutingNumber())
        assertEquals(expectedValue9,encoderTestSample7.encodeRoutingNumber())
        assertEquals(expectedValue0,encoderTestSample4.encodeRoutingNumber())
        assertEquals(expectedValue0,encoderTestSample8.encodeRoutingNumber())
    }
}