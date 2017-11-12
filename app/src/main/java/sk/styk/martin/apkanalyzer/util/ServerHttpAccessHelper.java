package sk.styk.martin.apkanalyzer.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Martin Styk on 12.11.2017.
 */

public class ServerHttpAccessHelper {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = ServerHttpAccessHelper.class.getName();
    private static final String URL = "http://192.168.1.37:8080/apkanalyzer/api/appdata";
    private OkHttpClient client = new OkHttpClient();

    public int postData(String json, String packageName) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Log.i(TAG, String.format("Response on posting %s data is %s", packageName, response.toString()));
        return response.code();
    }
}
