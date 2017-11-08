package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
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

    public Void doInBackground(AppDetailData... inputData) {

        Log.i(TAG, "Starting");
        AppDetailData data = inputData[0];

        if (data == null) {
            throw new IllegalArgumentException("data not specified");
        }

        Context context = contextWeakReference.get();
        if (context == null)
            return null;
        Log.i(TAG, "hash " + data.hashCode());

        if (SendDataService.isAlreadyUploaded(data, context)) {
            Log.i(TAG, "Data already uploaded");
            return null;
        }


        ServerSideAppData uploadData = new ServerSideAppData(data, AndroidIdHelper.getAndroidId(context));
        Log.i(TAG, "Converted to ServerSideAppData");

        String json = jsonSerializationUtils.serialize(uploadData);

        String targetPath = new File(Environment.getExternalStorageDirectory(), "data_" + System.currentTimeMillis() + ".json").getAbsolutePath();
        Log.i(TAG, "Start printing to " + targetPath);

        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(targetPath);
            printWriter.print(json);
            Thread.sleep(TimeUnit.SECONDS.toMillis(15));
            Log.i(TAG, "Saved to DB " + targetPath);
            SendDataService.insert(data, context);
            context = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null)
                printWriter.close();
        }

        Log.i(TAG, "Finished");

        return null;
    }

}