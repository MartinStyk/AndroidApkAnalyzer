package sk.styk.martin.apkanalyzer.util;

import com.google.gson.Gson;

/**
 * Created by Martin Styk on 06.11.2017.
 */
public class JsonSerializationUtils {

    private Gson gson;

    public JsonSerializationUtils() {
        this.gson = new Gson();
    }

    public String serialize(Object object) {
        return gson.toJson(object);
    }
}
