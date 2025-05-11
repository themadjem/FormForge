package com.themadjem.formforge.utils

import java.awt.FileDialog
import java.awt.Frame
import java.io.File

object FileSystemUtils {

    val imagesDirectory: File
        get() = File(getAppDataDir(), "images")

    val setupsDirectory: File
        get() = File(getAppDataDir(), "setups")


    fun getAppDataDir(): File {
        val userHome = System.getProperty("user.home")
        val os = System.getProperty("os.name").lowercase()

        return when {
            os.contains("win") -> File(System.getenv("APPDATA"), "FormForge")
            os.contains("mac") -> File(userHome, "Library/Application Support/FormForge")
            else -> File(userHome, ".config/FormForge")
        }
    }

    fun getAppDataResource(subdirectory: String, filename: String): File {
        val appdata = getAppDataDir()
        val subDir = File(appdata, subdirectory)
        if (!subDir.exists()) {
            val action = subDir.mkdirs()
            if (!action) {
                error("Error creating resource location $subdirectory")
            }
        }
        return File(subDir, filename)
    }

    fun getImage(path: String): File {
        return if (path.matches(Regex.fromLiteral("(^\\w:)|(^\\\\\\\\)|(^\\/)"))) {
            File(path)
        } else {
            getAppDataResource("images", path)
        }
    }

    fun selectFile(title: String = "Select File", startingDirectory: String? = null): File? {
        val fileDialog = FileDialog(Frame())
        fileDialog.title = title
        fileDialog.directory = startingDirectory
        fileDialog.isVisible = true
        return when {
            fileDialog.file.isNullOrEmpty() -> null
            else -> File(fileDialog.file)
        }
    }
}