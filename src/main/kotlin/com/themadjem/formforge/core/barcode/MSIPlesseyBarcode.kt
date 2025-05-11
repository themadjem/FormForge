package com.themadjem.formforge.core.barcode

import com.themadjem.formforge.core.barcode.IMbBarcode.IMbEncoder.BarType
import com.themadjem.formforge.utils.MathUtils
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage

class MSIPlesseyBarcode : Barcode() {
    override fun generate(data: String): RenderedImage {
        return renderMsi(encode(data))
    }

    val START = "110"
    val END = "100100"
    val dataMap = mapOf(
        Pair("0", "100100100100"),
        Pair("1", "100100100110"),
        Pair("2", "100100110100"),
        Pair("3", "100100110110"),
        Pair("4", "100110100100"),
        Pair("5", "100110100110"),
        Pair("6", "100110110100"),
        Pair("7", "100110110110"),
        Pair("8", "110100100100"),
        Pair("9", "110100100110")
    )

    fun encode(data: String): String {
        val checkdigit = getCheckDigit(data)
        val sb = StringBuilder(START)
        for (s in data) {
            sb.append(dataMap[s.toString()])
        }
        sb.append(dataMap[checkdigit])
        sb.append(END)
        return sb.toString()
    }

    fun getCheckDigit(data: String): String {
        var sum: Int = 0
        for ((index, value) in data.withIndex()) {
            var digit = value.digitToInt()
            if (index % 2 == 1) digit *= 2
            if (digit > 9) digit -= 9
            sum += digit
        }
        val check = (10 - (sum % 10)) % 10
        return check.toString()
    }

    fun renderMsi(encodedData: String): BufferedImage {
        val moduleHeight = MathUtils.inchesToPixels(0.5f)
        val moduleWidth = MathUtils.inchesToPixels(0.0166f)
        val horizontalQuietZone = MathUtils.inchesToPixels(0.125f)
        val verticalQuietZone = MathUtils.inchesToPixels(0.028f)

        val imgWidth = encodedData.length * moduleWidth + 2 * horizontalQuietZone
        val imgHeight = moduleHeight + (2 * verticalQuietZone)

        val image = BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        g.color = Color.WHITE
        g.fillRect(0, 0, image.width, image.height)
        g.color = Color.BLACK

        for ((i, bar) in encodedData.withIndex()) {
            if (bar == '0') continue
            val x = horizontalQuietZone + (i * moduleWidth)
            g.fillRect(x, verticalQuietZone, moduleWidth, moduleHeight)
        }
        g.dispose()
        return image
    }

    fun generateMsiSvg(encodedData: String): String {
        val moduleHeight = MathUtils.inchesToPixels(0.5f)
        val moduleWidth = MathUtils.inchesToPixels(0.0166f)
        val horizontalQuietZone = MathUtils.inchesToPixels(0.125f)
        val verticalQuietZone = MathUtils.inchesToPixels(0.028f)

        val imgWidth = encodedData.length * moduleWidth + 2 * horizontalQuietZone
        val imgHeight = moduleHeight + (2 * verticalQuietZone)


        val builder = StringBuilder()
        builder.append("<svg width=\"$imgWidth\" height=\"$imgHeight\" xmlns=\"http://www.w3.org/2000/svg\">\n")
        builder.append("<rect x=\"0\" y=\"0\" width=\"$imgWidth\" height=\"$imgHeight\" fill=\"white\"/>\n")

        for ((i, bar) in encodedData.withIndex()) {
            if (bar == '0') continue
            val x = horizontalQuietZone + (i * moduleWidth)
            builder.append("<rect x=\"$x\" y=\"${verticalQuietZone}\" width=\"$moduleWidth\" height=\"$moduleHeight\" fill=\"black\" />\n")
        }
        builder.append("</svg>")
        return builder.toString()
    }
}