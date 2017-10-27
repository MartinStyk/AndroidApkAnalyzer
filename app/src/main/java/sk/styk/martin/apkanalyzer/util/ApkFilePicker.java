package sk.styk.martin.apkanalyzer.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by Martin Styk on 09.10.2017.
 */

public class ApkFilePicker {

    public static final int REQUEST_PICK_APK = 1324;

    public static Intent getFilePickerIntent() {
        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
        mediaIntent.setType("application/vnd.android.package-archive");
        return mediaIntent;
    }

    public static String getPathFromIntentData(@NonNull Intent data, @NonNull Context context) {
        Uri apkUri = data.getData();
        File fileFromPath = new File(apkUri.getPath());
        if (fileFromPath.exists()) {
            return fileFromPath.getAbsolutePath();
        }

        String pathFromRealPathUtils = RealPathUtils.getPathFromUri(context, apkUri);
        if(pathFromRealPathUtils != null){
            File fileFromRealPathUtils = new File(pathFromRealPathUtils);
            if (fileFromRealPathUtils.exists()) {
                return fileFromRealPathUtils.getAbsolutePath();
            }
        }
        //we failed, return anything
        return apkUri.toString();
    }


}
