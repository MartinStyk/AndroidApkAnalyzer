package sk.styk.martin.apkanalyzer.util.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.OnInstallAppDetailActivity;

/**
 * Created by Martin Styk on 04.01.2018.
 */

public class ApkFileInstaller {

    @Nullable
    public Intent installPackage(Context context, String packagePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri apkUri = null;
        try {
            apkUri = FileProvider.getUriForFile(context, GenericFileProvider.AUTHORITY, new File(packagePath));
        } catch (Exception e) {
            Log.e(OnInstallAppDetailActivity.class.getSimpleName(), e.toString());
            Toast.makeText(context, context.getString(R.string.install_failed), Toast.LENGTH_LONG).show();
            return null;
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(intent);

        return intent;
    }


}
