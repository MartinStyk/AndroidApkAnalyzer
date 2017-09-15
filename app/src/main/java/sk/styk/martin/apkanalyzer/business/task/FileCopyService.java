package sk.styk.martin.apkanalyzer.business.task;

import android.app.IntentService;
import android.content.Intent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Martin Styk on 15.09.2017.
 */
public class FileCopyService extends IntentService {

    public static final String SOURCE_FILE = "s_file";
    public static final String TARGET_FILE = "t_file";

    // No-arg constructor is required
    public FileCopyService() {
        super(FileCopyService.class.getSimpleName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String sourcePath = intent.getStringExtra(SOURCE_FILE);
        String targetPath = intent.getStringExtra(TARGET_FILE);

        if (sourcePath == null || targetPath == null) {
            throw new IllegalArgumentException("source and target path not specified");
        }

        File source = new File(sourcePath);
        File target = new File(targetPath);

        try {
            copy(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void copy(File src, File dst) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }
}