
plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "com.themadjem.formforge"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.pdfbox:pdfbox:3.0.4")
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("FormForgeKt") // Entry point
}

tasks.test {
    useJUnitPlatform()
}
