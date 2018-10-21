package sk.styk.martin.apkanalyzer.util

import android.content.pm.PermissionInfo
import sk.styk.martin.apkanalyzer.ApkAnalyzer

/**
 * @author Martin Styk
 * @version 02.08.2017.
 */
object PermissionLevelHelper {

    @JvmStatic
    fun showLocalized(level: Int): String {

        val context = ApkAnalyzer.context

        return when (level) {
            PermissionInfo.PROTECTION_NORMAL -> context.getString(R.string.permissions_protection_normal)
            PermissionInfo.PROTECTION_DANGEROUS -> context.getString(R.string.permissions_protection_dangerous)
            PermissionInfo.PROTECTION_SIGNATURE -> context.getString(R.string.permissions_protection_signature)
            else -> context.getString(R.string.permissions_protection_normal)
        }
    }
}
