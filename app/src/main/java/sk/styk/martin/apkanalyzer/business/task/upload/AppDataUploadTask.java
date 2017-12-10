package sk.styk.martin.apkanalyzer.business.task.upload;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;

import sk.styk.martin.apkanalyzer.database.service.SendDataService;
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;
import sk.styk.martin.apkanalyzer.model.server.ServerSideAppData;
import sk.styk.martin.apkanalyzer.util.AndroidIdHelper;
import sk.styk.martin.apkanalyzer.util.networking.ConnectivityHelper;
import sk.styk.martin.apkanalyzer.util.JsonSerializationUtils;
import sk.styk.martin.apkanalyzer.util.networking.ServerHttpAccessHelper;
import sk.styk.martin.apkanalyzer.util.networking.UploadAppDataRestHelper;

/**
 * Created by Martin Styk on 6.11.2017.
 */
public class AppDataUploadTask extends AsyncTask<AppDetailData, Void, Void> {
    private static final String TAG = AppDataUploadTask.class.getSimpleName();

    private WeakReference<Context> contextWeakReference;
    private JsonSerializationUtils jsonSerializationUtils;


    public AppDataUploadTask(Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
        jsonSerializationUtils = new JsonSerializationUtils();
    }

    public Void doInBackground(@NonNull AppDetailData... inputData) {
        AppDetailData data = inputData[0];
        if (data == null) {
            Log.e(TAG, "Save task got parameter null");
            return null;
        }

        String packageName = data.getGeneralData().getPackageName();

        Log.i(TAG, "Starting save task for " + packageName);

        Context context = contextWeakReference.get();
        if (context == null)
            return null;

        Log.i(TAG, String.format("Using hash %03d for package %s", data.hashCode(), packageName));

        if (SendDataService.isAlreadyUploaded(data, context)) {
            Log.i(TAG, String.format("Package %s already uploaded", packageName));
            return null;
        }

        if (!ConnectivityHelper.isUploadPossible(context)) {
            Log.i(TAG, String.format("No internet access, aborting upload of %s", packageName));
            return null;
        }

        ServerSideAppData uploadData = new ServerSideAppData(data, AndroidIdHelper.getAndroidId(context));

        try {
            int responseCode = new UploadAppDataRestHelper().postData(jsonSerializationUtils.serialize(uploadData), packageName);
            // TODO do not insert to DB - for debug allow repeated uploads
//            SendDataService.insert(data, context);
            Log.i(TAG, String.format("Finished uploading package %s with response + %03d", packageName, responseCode));
        } catch (Exception e) {
            Log.w(TAG, String.format("Upload of package %s failed with exception %s", packageName, e.toString()));
        }


//        String targetPath = new File(Environment.getExternalStorageDirectory(), "data_" + System.currentTimeMillis() + ".json").getAbsolutePath();
//        Log.i(TAG, String.format("Start uploading package %s to %s", packageName, targetPath));
//
//        PrintWriter printWriter = null;
//
//        try {
//            printWriter = new PrintWriter(targetPath);
//            printWriter.print(json);
//            Thread.sleep(TimeUnit.SECONDS.toMillis(15));
//            SendDataService.insert(data, context);
//            Log.i(TAG, String.format("Finished uploading package %s to %s", packageName, targetPath));
//            context = null;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (printWriter != null)
//                printWriter.close();
//        }

        Log.i(TAG, "Finishing save task for " + packageName);

        return null;
    }

}