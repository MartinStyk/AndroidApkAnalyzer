package sk.styk.martin.apkanalyzer.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by Martin Styk on 15.09.2017.
 */
public interface ApkFileOperations {

    /**
     * Get APK file name which will be created on External Storage.
     * This method does not copy APK file, only returns correct absolute path and name format.
     * @param packageName of app
     * @return file representing APK file in external storage
     */
    File getFileApkFileOnExternalStorage(String packageName);

    /**
     * Copy file from sourcd to target
     * @param src source
     * @param dst target
     * @throws IOException if something fails
     */
    void copy(File src, File dst) throws IOException;

}
