package sk.styk.martin.apkanalyzer.util;

import android.content.Context;
import android.content.pm.PackageInfo;

import sk.styk.martin.apkanalyzer.R;

/**
 * @author Martin Styk
 * @version 02.08.2017.
 */
public class InstallLocationHelper {

    public static final String INSTALL_LOCATION_AUTO = "Auto";
    public static final String INSTALL_LOCATION_INTERNAL_ONLY = "Internal Only";
    public static final String INSTALL_LOCATION_PREFER_EXTERNAL = "Prefer External";

    public static String resolveInstallLocation(int installLocation) {
        switch (installLocation) {
            case PackageInfo.INSTALL_LOCATION_AUTO:
                return INSTALL_LOCATION_AUTO;
            case PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY:
                return INSTALL_LOCATION_INTERNAL_ONLY;
            case PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL:
                return INSTALL_LOCATION_PREFER_EXTERNAL;
            default:
                return INSTALL_LOCATION_INTERNAL_ONLY;
        }
    }

    public static String showLocalizedLocation(String installLocation, Context context) {
        switch (installLocation) {
            case INSTALL_LOCATION_AUTO:
                return context.getString(R.string.install_loc_auto);
            case INSTALL_LOCATION_INTERNAL_ONLY:
                return context.getString(R.string.install_loc_internal_only);
            case INSTALL_LOCATION_PREFER_EXTERNAL:
                return context.getString(R.string.install_loc_prefer_external);
            default:
                return context.getString(R.string.install_loc_internal_only);
        }
    }
}
