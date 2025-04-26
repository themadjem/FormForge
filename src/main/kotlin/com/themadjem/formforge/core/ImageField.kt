package com.themadjem.formforge.core

import org.apache.pdfbox.pdmodel.PDPage

open class ImageField(
    override val name: String,
    override val x: Float,
    override val y: Float,

) :Field() {
    override fun render(
        pdfPDPage: PDPage,
        data: Map<String, String>
    ) {
        TODO("Not yet implemented")
    }
}