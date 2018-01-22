package sk.styk.martin.apkanalyzer.util

import android.content.Context
import android.content.pm.PackageInfo
import sk.styk.martin.apkanalyzer.ApkAnalyzer

import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk
 * @version 02.08.2017.
 */
object InstallLocationHelper {

    private const val INSTALL_LOCATION_AUTO = "Auto"
    private const val INSTALL_LOCATION_INTERNAL_ONLY = "Internal Only"
    private const val INSTALL_LOCATION_PREFER_EXTERNAL = "Prefer External"

    fun resolveInstallLocation(installLocation: Int): String {
        return when (installLocation) {
            PackageInfo.INSTALL_LOCATION_AUTO -> INSTALL_LOCATION_AUTO
            PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY -> INSTALL_LOCATION_INTERNAL_ONLY
            PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL -> INSTALL_LOCATION_PREFER_EXTERNAL
            else -> INSTALL_LOCATION_INTERNAL_ONLY
        }
    }

    @JvmStatic
    fun showLocalizedLocation(installLocation: String): String {
        val context = ApkAnalyzer.context

        return when (installLocation) {
            INSTALL_LOCATION_AUTO -> context.getString(R.string.install_loc_auto)
            INSTALL_LOCATION_INTERNAL_ONLY -> context.getString(R.string.install_loc_internal_only)
            INSTALL_LOCATION_PREFER_EXTERNAL -> context.getString(R.string.install_loc_prefer_external)
            else -> context.getString(R.string.install_loc_internal_only)
        }
    }
}
