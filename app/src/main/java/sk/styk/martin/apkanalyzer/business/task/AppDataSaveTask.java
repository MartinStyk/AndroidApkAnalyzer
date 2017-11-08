package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import sk.styk.martin.apkanalyzer.database.service.SendDataService;
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;
import sk.styk.martin.apkanalyzer.model.server.ServerSideAppData;
import sk.styk.martin.apkanalyzer.util.AndroidIdHelper;
import sk.styk.martin.apkanalyzer.util.JsonSerializationUtils;

/**
 * Created by Martin Styk on 6.11.2017.
 */
public class AppDataSaveTask extends AsyncTask<AppDetailData, Void, Void> {
    private static final String TAG = AppDataSaveTask.class.getSimpleName();

    private WeakReference<Context> contextWeakReference;
    private JsonSerializationUtils jsonSerializationUtils;


    public AppDataSaveTask(Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
        jsonSerializationUtils = new JsonSerializationUtils();
    }

    public Void doInBackground(@NonNull AppDetailData... inputData) {
        AppDetailData data = inputData[0];
        if (data == null) {
            throw new IllegalArgumentException("data not specified");
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

        ServerSideAppData uploadData = new ServerSideAppData(data, AndroidIdHelper.getAndroidId(context));
        String json = jsonSerializationUtils.serialize(uploadData);
        String targetPath = new File(Environment.getExternalStorageDirectory(), "data_" + System.currentTimeMillis() + ".json").getAbsolutePath();
        Log.i(TAG, String.format("Start uploading package %s to %s", packageName, targetPath));

        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(targetPath);
            printWriter.print(json);
            Thread.sleep(TimeUnit.SECONDS.toMillis(15));
            SendDataService.insert(data, context);
            Log.i(TAG, String.format("Finished uploading package %s to %s", packageName, targetPath));
            context = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null)
                printWriter.close();
        }

        Log.i(TAG, "Finishing save task for " + packageName);

        return null;
    }

}