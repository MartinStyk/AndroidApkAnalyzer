package sk.styk.martin.apkanalyzer.util.networking;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static sk.styk.martin.apkanalyzer.util.networking.ServerUrls.UPLOAD_RECORD_PATH;
import static sk.styk.martin.apkanalyzer.util.networking.ServerUrls.URL_BASE;

/**
 * Created by Martin Styk on 10.12.2017.
 */

public class UploadAppDataRestHelper extends ServerHttpAccessHelper {
    private static final String TAG = ServerHttpAccessHelper.class.getName();

    public int postData(@NonNull String json, @NonNull String packageName) throws IOException {
        RequestBody body = RequestBody.create(JSON, zipContent(json).toByteArray());

        Request request = new Request.Builder()
                .url(URL_BASE + UPLOAD_RECORD_PATH)
                .post(body)
                .header("Content-Encoding", "gzip")
                .build();

        Response response = client.newCall(request).execute();
        int responseCode =  response.code();

        Log.i(TAG, String.format("Response on posting %s data is %s", packageName, response.toString()));

        response.close();
        return responseCode;
    }

}
