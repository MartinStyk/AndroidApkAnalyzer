package sk.styk.martin.apkanalyzer.util.file

import android.support.v4.content.FileProvider

/**
 * @author Martin Styk
 * @version 27.10.2017.
 */
class GenericFileProvider : FileProvider() {
    companion object {
        const val AUTHORITY = "sk.styk.martin"
    }
}