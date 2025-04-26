
plugins {
    kotlin("jvm") version "2.0.0"  // Or whatever latest stable
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.pdfbox:pdfbox:2.0.30")  // PDFBox dependency
}

application {
    mainClass.set("FormForgeKt") // Entry point
}
