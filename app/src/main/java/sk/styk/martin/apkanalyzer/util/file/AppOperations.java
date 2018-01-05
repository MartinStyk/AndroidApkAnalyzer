package sk.styk.martin.apkanalyzer.util.file;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.OnInstallAppDetailActivity;

/**
 * Created by Martin Styk on 04.01.2018.
 */

public class AppOperations {

    public void installPackage(@NonNull Context context, @NonNull String packagePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri apkUri = null;
        try {
            apkUri = FileProvider.getUriForFile(context, GenericFileProvider.AUTHORITY, new File(packagePath));
        } catch (Exception e) {
            Log.e(OnInstallAppDetailActivity.class.getSimpleName(), e.toString());
            Toast.makeText(context, context.getString(R.string.install_failed), Toast.LENGTH_LONG).show();
            return;
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(intent);
    }

    public void openAppSystemPage(@NonNull Context context, @NonNull String packageName){
        Intent systemInfoIntent = new Intent();
        systemInfoIntent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        systemInfoIntent.setData(Uri.parse("package:" + packageName));

        context.startActivity(systemInfoIntent);
    }

    public void shareApkFile(@NonNull Context context, @NonNull String pathToApk){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(new File(pathToApk));

        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("application/vnd.android.package-archive");

        try {
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_apk_using)));
        } catch (ActivityNotFoundException e) {
            // this might happen on Android 4.4
            Toast.makeText(context, context.getString(R.string.activity_not_found_sharing), Toast.LENGTH_LONG).show();
        }
    }

    public void openGooglePlay(Context context, String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException anfe) {
            // Google Play not installed, open it in browser
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

}
