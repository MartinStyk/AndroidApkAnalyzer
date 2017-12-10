package sk.styk.martin.apkanalyzer.util.networking;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by Martin Styk on 10.12.2017.
 */
public class BasicAuthInterceptor implements Interceptor {

    private String credentials;
    private static String user = "device";
    private static String password = "******";

    public BasicAuthInterceptor() {
        this.credentials = Credentials.basic(user, password);
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials).build();
        return chain.proceed(authenticatedRequest);
    }

}