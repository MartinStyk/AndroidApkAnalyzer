package sk.styk.martin.apkanalyzer.business.task;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;

import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;
import sk.styk.martin.apkanalyzer.model.detail.FileData;
import sk.styk.martin.apkanalyzer.model.server.ServerSideAppData;
import sk.styk.martin.apkanalyzer.util.AndroidIdHelper;
import sk.styk.martin.apkanalyzer.util.JsonSerializationUtils;

/**
 * Created by Martin Styk on 6.11.2017.
 */
public class AppDataSaveService extends IntentService {

    private static final String TAG = AppDataSaveService.class.getSimpleName();

    public static final String APP_DETAIL_DATA = "app_detail_data";
    public static final String TARGET_FILE = "t_file";

    private JsonSerializationUtils jsonSerializationUtils;

    public AppDataSaveService() {
        super(AppDataSaveService.class.getSimpleName());
        jsonSerializationUtils = new JsonSerializationUtils();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Starting");
        AppDetailData data = intent.getParcelableExtra(APP_DETAIL_DATA);
        String targetPath = intent.getStringExtra(TARGET_FILE);

        if (data == null || targetPath == null) {
            Log.e(TAG, "source string or target path not specified");
            throw new IllegalArgumentException("source string or target path not specified");
        }

        ServerSideAppData uploadData = new ServerSideAppData(data, AndroidIdHelper.getAndroidId(getApplicationContext()));
        Log.i(TAG, "Converted to ServerSideAppData");

        String json = jsonSerializationUtils.serialize(uploadData);
        Log.i(TAG, "Serialized to json");

        PrintWriter printWriter = null;

        Log.i(TAG, "Start printing to " + targetPath);

        try {
            printWriter = new PrintWriter(targetPath);
            printWriter.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null)
                printWriter.close();
        }

        Log.i(TAG, "Finished");

    }

}