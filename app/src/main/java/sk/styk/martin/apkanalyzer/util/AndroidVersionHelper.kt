package sk.styk.martin.apkanalyzer.util

import sk.styk.martin.apkanalyzer.ApkAnalyzer
import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk
 * @version 02.08.2017
 */
object AndroidVersionHelper {

    const val MAX_SDK_VERSION = 27

    fun resolveVersion(sdkVersion: Int): String? {
        //java index from 0 - first item is sdk 1
        val index = sdkVersion - 1

        val versions = ApkAnalyzer.getContext().resources.getStringArray(R.array.android_versions)

        return if (index >= 0 && index < versions.size) versions[index] else null
    }
}
