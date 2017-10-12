package sk.styk.martin.apkanalyzer.model.detail;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Martin Styk on 09.10.2017.
 */

public enum AppSource {

    GOOGLE_PLAY("Google Play", "com.android.vending"),
    AMAZON_STORE("Amazon App Store", "com.amazon.venezia"),
    SYSTEM_PREINSTALED("Pre-installed"),
    UNKNOWN("Unknown");

    private String installerPackageName;
    private String name;

    AppSource(String name) {
        this.name = name;
    }

    AppSource(String name, String installerPackageName) {
        this.name = name;
        this.installerPackageName = installerPackageName;
    }

    @Nullable
    public static AppSource get(@NonNull PackageManager packageManager, @NonNull String packageName, boolean isSystem) {
        String installer;
        try {
            installer = packageManager.getInstallerPackageName(packageName);
        } catch (Exception e) {
            //this means package is not installed
            return null;
        }
        return AppSource.fromInstallerPackage(installer, isSystem);
    }

    private static AppSource fromInstallerPackage(String actualInstaller, boolean isSystem) {
        if (GOOGLE_PLAY.installerPackageName.equals(actualInstaller))
            return GOOGLE_PLAY;
        else if (AMAZON_STORE.installerPackageName.equals(actualInstaller))
            return AMAZON_STORE;
        else if (isSystem)
            return SYSTEM_PREINSTALED;
        else
            return UNKNOWN;
    }

    @Override
    public String toString() {
        return name;
    }
}
