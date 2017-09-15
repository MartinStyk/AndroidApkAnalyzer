package sk.styk.martin.apkanalyzer.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Martin Styk on 15.09.2017.
 */

public class ApkFileOperationsImpl implements ApkFileOperations {

    public File getFileApkFileOnExternalStorage(String packageName) {
        return new File(Environment.getExternalStorageDirectory(), packageName + ".apk");
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);

            byte[] buf = new byte[10024];
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
