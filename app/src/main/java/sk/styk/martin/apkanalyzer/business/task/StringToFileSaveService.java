package sk.styk.martin.apkanalyzer.business.task;

import android.app.IntentService;
import android.content.Intent;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Martin Styk on 15.09.2017.
 */
public class StringToFileSaveService extends IntentService {

    public static final String SOURCE_STRING = "s_string";
    public static final String TARGET_FILE = "t_file";

    public StringToFileSaveService() {
        super(StringToFileSaveService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String sourceString = intent.getStringExtra(SOURCE_STRING);
        String targetPath = intent.getStringExtra(TARGET_FILE);

        if (sourceString == null || targetPath == null) {
            throw new IllegalArgumentException("source string or target path not specified");
        }

        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(targetPath);
            printWriter.print(sourceString);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null)
                printWriter.close();
        }

    }

}