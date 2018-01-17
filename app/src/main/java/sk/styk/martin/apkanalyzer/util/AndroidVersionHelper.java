package sk.styk.martin.apkanalyzer.util;

import android.content.Context;

import sk.styk.martin.apkanalyzer.ApkAnalyzer;
import sk.styk.martin.apkanalyzer.R;

/**
 * @author Martin Styk
 * @version 02.08.2017
 */
public class AndroidVersionHelper {

    public static final int MAX_SDK_VERSION = 27;

    public static String resolveVersion(int sdkVersion) {
        //java index from 0 - first item is sdk 1
        int index = sdkVersion - 1;

        Context context = ApkAnalyzer.getContext();
        String[] versions = context.getResources().getStringArray(R.array.android_versions);

        return (index >= 0 && index < versions.length) ? versions[index] : null;
    }
}
