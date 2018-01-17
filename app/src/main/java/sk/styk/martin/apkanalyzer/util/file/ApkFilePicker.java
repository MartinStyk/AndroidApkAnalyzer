package sk.styk.martin.apkanalyzer.util.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * @author Martin Styk
 * @version 09.10.2017.
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
        if (apkUri == null)
            return null;

        File fileFromPath = new File(apkUri.getPath());
        if (fileFromPath.exists()) {
            return fileFromPath.getAbsolutePath();
        }

        String pathFromRealPathUtils = RealPathUtils.getPathFromUri(context, apkUri);
        if(pathFromRealPathUtils != null){
            try {
                File fileFromRealPathUtils = new File(pathFromRealPathUtils);
                if (fileFromRealPathUtils.exists()) {
                    return fileFromRealPathUtils.getAbsolutePath();
                }
            } catch (Exception e){
                // sometimes new File call might throw an exception
                //we failed, return anything
                return apkUri.toString();
            }

        }
        //we failed, return anything
        return apkUri.toString();
    }


}
