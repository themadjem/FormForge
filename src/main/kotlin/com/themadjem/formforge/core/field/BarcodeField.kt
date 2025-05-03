package com.themadjem.formforge.core.field

import com.themadjem.formforge.core.barcode.IMbBarcode
import org.apache.pdfbox.pdmodel.PDPage

class BarcodeField(
    override val name: String,
    override val x: Float,
    override val y: Float,
    val barcodeType: BarcodeType
) : ImageField(name, x, y) {
    override fun render(pdfPDPage: PDPage, data: Map<String, String>) {


        val generatedImage = when (barcodeType) {
            BarcodeType.IMb -> IMbBarcode().generate(data.get("barcode")!!)
            else -> {}
        }

        TODO("Not yet implemented")
    }
}