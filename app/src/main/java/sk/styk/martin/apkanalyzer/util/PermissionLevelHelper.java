package sk.styk.martin.apkanalyzer.util;

import android.content.Context;
import android.content.pm.PermissionInfo;

import sk.styk.martin.apkanalyzer.R;

/**
 * @author Martin Styk
 * @version 02.08.2017.
 */
public class PermissionLevelHelper {

    public static String showLocalized(int level, Context context) {
        switch (level) {
            case PermissionInfo.PROTECTION_NORMAL:
                return context.getString(R.string.permissions_protection_normal);
            case PermissionInfo.PROTECTION_DANGEROUS:
                return context.getString(R.string.permissions_protection_dangerous);
            case PermissionInfo.PROTECTION_SIGNATURE:
                return context.getString(R.string.permissions_protection_signature);
            default:
                return context.getString(R.string.permissions_protection_normal);
        }
    }
}
