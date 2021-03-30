package sk.styk.martin.apkanalyzer.util

import android.content.pm.PackageInfo

object InstallLocationHelper {

    const val INSTALL_LOCATION_AUTO = "Auto"
    const val INSTALL_LOCATION_INTERNAL_ONLY = "Internal Only"
    const val INSTALL_LOCATION_PREFER_EXTERNAL = "Prefer External"

    fun resolveInstallLocation(installLocation: Int): String {
        return when (installLocation) {
            PackageInfo.INSTALL_LOCATION_AUTO -> INSTALL_LOCATION_AUTO
            PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY -> INSTALL_LOCATION_INTERNAL_ONLY
            PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL -> INSTALL_LOCATION_PREFER_EXTERNAL
            else -> INSTALL_LOCATION_INTERNAL_ONLY
        }
    }

}
