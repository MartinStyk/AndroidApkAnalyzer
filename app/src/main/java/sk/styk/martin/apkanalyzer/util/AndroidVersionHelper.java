package sk.styk.martin.apkanalyzer.util;

/**
 * Created by Martin Styk on 02.08.2017.
 */

public class AndroidVersionHelper {

    public static final int MAX_SDK_VERSION = 27;

    public static String resolveVersion(int sdkVersion) {
        //java index from 0 - first item is sdk 1
        int index = sdkVersion - 1;
        String[] version = {
                "Android 1",
                "Android 1.1 Petit Four",
                "Android 1.5 Cupcake",
                "Android 1.6 Donut",
                "Android 2.0 Eclair",
                "Android 2.0.1 Eclair",
                "Android 2.1 Eclair",
                "Android 2.2 Froyo",
                "Android 2.3 Gingerbread",
                "Android 2.3.3 Gingerbread",
                "Android 3.0 Honeycomb",
                "Android 3.1 Honeycomb",
                "Android 3.2 Honeycomb",
                "Android 4.0 Ice Cream Sandwich",
                "Android 4.0.3 Ice Cream Sandwich",
                "Android 4.1 Jelly Bean",
                "Android 4.2 Jelly Bean",
                "Android 4.3 Jelly Bean",
                "Android 4.4 KitKat",
                "Android 4.4W KitKat",
                "Android 5.0 Lollipop",
                "Android 5.1.1 Lollipop",
                "Android 6.0 Marshmallow",
                "Android 7.0 Nougat",
                "Android 7.1.2 Nougat",
                "Android 8.0 Oreo",
                "Android 8.1 Oreo"
        };
        return (index >= 0 && index < version.length) ? version[index] : null;
    }
}
