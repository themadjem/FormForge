package com.themadjem.formforge.core.barcode

import com.themadjem.formforge.utils.FileSystemUtils
import jogamp.common.os.elf.SectionArmAttributes
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.FileWriter
import java.io.OutputStream
import java.io.Writer
import javax.imageio.ImageIO
import java.io.File

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

fun main() {
    val barc = MSIPlesseyBarcode()
    val img = barc.generate("1789372997")
    val out = File(FileSystemUtils.imagesDirectory, "msi.png")
    ImageIO.write(img, "png", out)
}