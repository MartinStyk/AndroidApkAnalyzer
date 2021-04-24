package sk.styk.martin.apkanalyzer.manager.appanalysis

import android.content.pm.PackageInfo
import dalvik.system.DexFile
import sk.styk.martin.apkanalyzer.model.detail.ClassPathData
import sk.styk.martin.apkanalyzer.util.TAG_APP_ANALYSIS
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
class DexManager @Inject constructor(){

    fun get(packageInfo: PackageInfo): ClassPathData {

        val packageClasses = ArrayList<String>()
        val otherClasses = ArrayList<String>()
        var innerClasses = 0

        if (packageInfo.applicationInfo != null) {

            var dexFile: DexFile? = null
            try {
                dexFile = DexFile(packageInfo.applicationInfo.sourceDir)
                val iterator = dexFile.entries()
                while (iterator.hasMoreElements()) {
                    val className = iterator.nextElement()
                    if (className != null && className.startsWith(packageInfo.applicationInfo.packageName))
                        packageClasses.add(className)
                    else
                        otherClasses.add(className)

                    if (className != null && className.contains("$")) {
                        innerClasses++
                    }
                }
            } catch (e: IOException) {
                Timber.tag(TAG_APP_ANALYSIS).w(e, "Can not read dex info")
            } finally {
                try {
                    dexFile?.close()
                } catch (e: IOException) {
                    Timber.tag(TAG_APP_ANALYSIS).w(e, "Can not close dex file")
                }
            }
        }

        return ClassPathData(
                packageClasses = packageClasses,
                otherClasses = otherClasses,
                numberOfInnerClasses = innerClasses
        )
    }
}
