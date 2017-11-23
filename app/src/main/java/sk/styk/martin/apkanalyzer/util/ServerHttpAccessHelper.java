package sk.styk.martin.apkanalyzer.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

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
    private static final String URL = "https://apk-analyzer.herokuapp.com/app_records";
    private OkHttpClient client = new OkHttpClient();

    public int postData(@NonNull String json, @NonNull String packageName) throws IOException {
        RequestBody body = RequestBody.create(JSON, zipContent(json).toByteArray());

        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .header("Content-Encoding", "gzip")
                .build();

        Response response = client.newCall(request).execute();
        
        Log.i(TAG, String.format("Response on posting %s data is %s", packageName, response.toString()));
        return response.code();
    }

    @NonNull
    private ByteArrayOutputStream zipContent(@NonNull String json) {
        OutputStream zipper = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] data = json.getBytes("UTF-8");
            zipper = new GZIPOutputStream(outputStream);
            zipper.write(data);

        } catch (Exception e) {
            return null;
        } finally {
            if (zipper != null) {
                try {
                    zipper.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outputStream;
    }
}
