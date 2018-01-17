package sk.styk.martin.apkanalyzer.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author Martin Styk
 * @version 03.01.2018.
 */

public class FileUtils {

    public static void copy(File src, File dst) throws IOException {
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

    public static void writeString(String content, String targetFilePath) throws IOException {
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(targetFilePath);
            printWriter.print(content);
        } finally {
            if (printWriter != null)
                printWriter.close();
        }

    }
}
