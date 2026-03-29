package sk.styk.martin.apkanalyzer.util.file

import androidx.core.content.FileProvider

class GenericFileProvider : FileProvider() {
    companion object {
        val AUTHORITY = "sk.styk.martin.apkanalyzer"
    }
}
