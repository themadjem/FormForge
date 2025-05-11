package com.themadjem.formforge.core.barcode

import com.themadjem.formforge.utils.FileSystemUtils
import com.themadjem.formforge.utils.MathUtils
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage

class Code11Barcode : Barcode() {
    override fun generate(data: String): RenderedImage {
        return renderC11(encode(data))
    }

    val STARTSTOP = "10110010"
    val dataMap = mapOf<String, String>(
        Pair("0", "1010110"),
        Pair("1", "11010110"),
        Pair("2", "10010110"),
        Pair("3", "11001010"),
        Pair("4", "10110110"),
        Pair("5", "11011010"),
        Pair("6", "10011010"),
        Pair("7", "10100110"),
        Pair("8", "11010010"),
        Pair("9", "1101010"),
        Pair("-", "1011010"),
    )

    fun encode(data: String): String {
        val sb = StringBuilder(STARTSTOP)
        for (s in data) {
            sb.append(dataMap[s.toString()])
        }
        sb.append(STARTSTOP)
        return sb.toString()
    }

    fun renderC11(encodedData: String): BufferedImage {
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

    fun generateC11Svg(encodedData: String): String {
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
