package com.themadjem.formforge.core.barcode

import com.themadjem.formforge.utils.FileSystemUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import javax.imageio.ImageIO

class MSIPlesseyBarcodeTest {
    @Test
    fun encode() {
    }

    @Test
    fun getCheckDigit() {
        val expected = "4"
        val actual = MSIPlesseyBarcode().getCheckDigit("1789372997")
        assertEquals(expected, actual)
    }

}