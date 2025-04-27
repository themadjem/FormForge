package com.themadjem.formforge

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font
import java.io.File

fun main() {
    println("FormForge starting...")

    val templatePath = "template.pdf" // This should be an existing PDF file
    val outputPath = "filled_form.pdf"

//    val document = PDDocument.load(File(templatePath))
//    val page = document.getPage(0)
//
//    PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true).use { contentStream ->
//        contentStream.beginText()
//        contentStream.setFont(, 14f)
//        contentStream.newLineAtOffset(100f, 700f)
//        contentStream.showText("Hello, this is FormForge!")
//        contentStream.endText()
//    }
//
//    document.save(outputPath)
//    document.close()

    println("Form generated at $outputPath 🎉")
}
