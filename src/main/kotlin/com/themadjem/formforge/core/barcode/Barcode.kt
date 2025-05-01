package com.themadjem.formforge.core.barcode

import java.awt.Image
import java.awt.image.RenderedImage

abstract class Barcode {
    abstract fun generate(data:String): RenderedImage
}
