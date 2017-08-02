package sk.styk.martin.apkanalyzer.util;

import android.content.Context;
import android.content.pm.PackageInfo;

import sk.styk.martin.apkanalyzer.R;

/**
 * Created by Martin Styk on 02.08.2017.
 */

public class InstallLocationHelper {

    public static final String INSTALL_LOCATION_AUTO = "Auto";
    public static final String INSTALL_LOCATION_INTERNAL_ONLY = "Internal Only";
    public static final String INSTALL_LOCATION_PREFER_EXTERNAL = "Prefer External";

    public static String resolveInstallLocation(int installLocation) {
        return resolveInstallLocation(installLocation, null);
    }

    public static String resolveInstallLocation(int installLocation, Context context) {
        switch (installLocation) {
            case PackageInfo.INSTALL_LOCATION_AUTO:
                return context != null ? context.getString(R.string.install_loc_auto) : INSTALL_LOCATION_AUTO;
            case PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY:
                return context != null ? context.getString(R.string.install_loc_internal_only) : INSTALL_LOCATION_INTERNAL_ONLY;
            case PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL:
                return context != null ? context.getString(R.string.install_loc_preffer_external) : INSTALL_LOCATION_PREFER_EXTERNAL;
            default:
                return context != null ? context.getString(R.string.install_loc_internal_only) : INSTALL_LOCATION_INTERNAL_ONLY;
        }
    }
}
