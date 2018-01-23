package sk.styk.martin.apkanalyzer.util

import android.content.pm.PermissionInfo
import sk.styk.martin.apkanalyzer.ApkAnalyzer
import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk
 * @version 02.08.2017.
 */
object PermissionLevelHelper {

    @JvmStatic
    fun showLocalized(level: Int): String {

        val context = ApkAnalyzer.context

        when (level) {
            PermissionInfo.PROTECTION_NORMAL -> return context.getString(R.string.permissions_protection_normal)
            PermissionInfo.PROTECTION_DANGEROUS -> return context.getString(R.string.permissions_protection_dangerous)
            PermissionInfo.PROTECTION_SIGNATURE -> return context.getString(R.string.permissions_protection_signature)
            else -> return context.getString(R.string.permissions_protection_normal)
        }
    }
}
