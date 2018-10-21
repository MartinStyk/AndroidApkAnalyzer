package sk.styk.martin.apkanalyzer.util

import sk.styk.martin.apkanalyzer.ApkAnalyzer

/**
 * @author Martin Styk
 * @version 02.08.2017
 */
object AndroidVersionHelper {

    const val MAX_SDK_VERSION = 28

    fun resolveVersion(sdkVersion: Int?): String? {
        if (sdkVersion == null) return null
        //java index from 0 - first item is sdk 1
        val index = sdkVersion - 1

        val versions = ApkAnalyzer.context.resources.getStringArray(R.array.android_versions)

        return if (index >= 0 && index < versions.size) versions[index] else null
    }
}
