package com.themadjem.formforge.core.barcode

enum class BarcodeType {
    UPC, // ISO/IEC 15420 Universal Product Code
    EAN, // ISO/IEC 15420 European Article Number
    Code39, // ISO/IEC 16388 Code 39
    Code128, // ISO/IEC 15417 Code 128
    Code25, // ISO/IEC 16390 Interleaved 2 of 5
    Code93, // Code 93
    Codabar, // Codabar
    GS1DataBar, // GS1 §5.5 GS1 DataBar
    MSIPlessey, // MSI aka Modified Plessey
    QR, // SO/IEC 18004 QR Code
    DataMatrix, // ISO/IEC 16022-2006 DataMatrix
    PDF417, // ISO/IEC 15438 Portable Data File 4-17
    Aztec, // ISO/IEC 24778 Aztec
    DAFT, // Generic 4-state bar style barcode used by many postal services
    IMb, // USPS-B-3200 Intelligent Mail Barcode
    PLANET, // USPS PLANET
    POSTNET, // USPS POSTNET
    MaxiCode, // ISO/IEC 16023 UPS MaxiCode
    FIM, // USPS Facing Identification Mark
    ITF, // GS1 §5.3 Interleaved 2 of 5
}