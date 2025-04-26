package com.themadjem.formforge.core.barcode

import java.awt.Image

abstract class Barcode {
    abstract fun generate(data:String): Image
}
