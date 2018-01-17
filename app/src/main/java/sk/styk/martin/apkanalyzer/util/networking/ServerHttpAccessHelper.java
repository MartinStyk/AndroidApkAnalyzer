package sk.styk.martin.apkanalyzer.util.networking;

import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * @author Martin Styk
 * @version 12.11.2017.
 */

public class ServerHttpAccessHelper {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client;

    public ServerHttpAccessHelper() {
        client = initClient();
    }


    @NonNull
    protected ByteArrayOutputStream zipContent(@NonNull String json) {
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

    private OkHttpClient initClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor())
                .build();
    }
}
