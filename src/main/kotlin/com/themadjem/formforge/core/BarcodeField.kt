package com.themadjem.formforge.core

import org.apache.pdfbox.pdmodel.PDPage

class BarcodeField(
    override val name: String,
    override val x: Float,
    override val y: Float,
    val barcodeType: BarcodeType
) : ImageField(name, x, y) {
    override fun render(pdfPDPage: PDPage, data: Map<String, String>) {
        TODO("Not yet implemented")
    }
}