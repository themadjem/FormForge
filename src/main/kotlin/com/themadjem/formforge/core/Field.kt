package com.themadjem.formforge.core
import org.apache.pdfbox.pdmodel.PDPage

abstract class Field {
    abstract val name: String        // internal name for the field
    abstract val x: Float             // x position on the page
    abstract val y: Float             // y position on the page


    abstract fun render(pdfPDPage: PDPage, data: Map<String, String>)
}
