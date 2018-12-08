package sk.styk.martin.apkanalyzer.util.file

import android.support.v4.content.FileProvider
import sk.styk.martin.apkanalyzer.util.AppFlavour

/**
 * @author Martin Styk
 * @version 27.10.2017.
 */
class GenericFileProvider : FileProvider() {
    companion object {
        val AUTHORITY = if (AppFlavour.isPremium)  "sk.styk.martin.apkanalyzer.premium" else "sk.styk.martin.apkanalyzer"
    }
}