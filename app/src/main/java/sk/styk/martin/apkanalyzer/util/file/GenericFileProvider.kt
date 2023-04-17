package sk.styk.martin.apkanalyzer.util.file

import androidx.core.content.FileProvider
import sk.styk.martin.apkanalyzer.util.AppFlavour

class GenericFileProvider : FileProvider() {
    companion object {
        val AUTHORITY = if (AppFlavour.isPremium) "sk.styk.martin.apkanalyzer.premium" else "sk.styk.martin.apkanalyzer"
    }
}
