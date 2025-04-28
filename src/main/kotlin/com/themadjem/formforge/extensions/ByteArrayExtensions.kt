package com.themadjem.formforge.extensions

import kotlin.experimental.inv


fun ByteArray.invertAt(index: Int) {
    require(index in indices) { "Index must be within the bounds of the ByteArray" }
    this[index] = this[index].inv()
}
