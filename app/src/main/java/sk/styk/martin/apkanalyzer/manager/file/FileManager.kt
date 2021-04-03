package sk.styk.martin.apkanalyzer.manager.file

import android.content.Context
import android.os.Environment
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ApplicationScope
import java.io.IOException
import javax.inject.Inject

class FileManager @Inject constructor(@ApplicationScope private val context: Context) {

    val externalDirectory by lazy {
        context.getExternalFilesDir(null) ?: throw IOException("External directory not available")
    }

    val dcimDirectory by lazy {
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ?: throw IOException("Pictures directory not available")
    }

}