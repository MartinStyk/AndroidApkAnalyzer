package sk.styk.martin.apkanalyzer.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

/**
 * @author Martin Styk
 * @version 06.11.2017.
 */
public class JsonSerializationUtils {

    private Gson gson;

    public JsonSerializationUtils() {
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd HH:mm a z").create();
    }

    public String serialize(Object object) {
        return gson.toJson(object);
    }
}
