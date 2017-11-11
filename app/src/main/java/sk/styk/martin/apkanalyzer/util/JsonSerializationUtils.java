package sk.styk.martin.apkanalyzer.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

/**
 * Created by Martin Styk on 06.11.2017.
 */
public class JsonSerializationUtils {

    private Gson gson;

    public JsonSerializationUtils() {
        this.gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
    }

    public String serialize(Object object) {
        return gson.toJson(object);
    }
}
