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
 * Encapsulates workflow logic for uploading single app data to server.
 *
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

        if (SendDataService.isAlreadyUploaded(data, context)) {
            Log.i(TAG, String.format("Package %s already uploaded", packageName));
            return null;
        }

        if (!ConnectivityHelper.isUploadPossible(context)) {
            Log.i(TAG, String.format("Upload not possible, aborting upload of %s", packageName));
            return null;
        }

        ServerSideAppData uploadData = new ServerSideAppData(data, AndroidIdHelper.getAndroidId(context));

        try {
            int responseCode = new UploadAppDataRestHelper().postData(jsonSerializationUtils.serialize(uploadData), packageName);
            if(responseCode == 201){
                SendDataService.insert(data, context);
                Log.i(TAG, String.format("Upload of package %s successful", packageName));
            }
            if(responseCode == 409){
                SendDataService.insert(data, context);
                Log.i(TAG, String.format("Package %s was already uplaoded, however client is not aware of it", packageName));
            }
            Log.i(TAG, String.format("Finished uploading package %s with response + %03d", packageName, responseCode));
        } catch (Exception e) {
            Log.w(TAG, String.format("Upload of package %s failed with exception %s", packageName, e.toString()));
        }

        Log.i(TAG, "Finishing save task for " + packageName);

        return null;
    }

}