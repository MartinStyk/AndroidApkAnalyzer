package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.PackageInfo;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * Created by Martin Styk on 21.10.2017.
 */
public class DexService {

    public List<String> get(PackageInfo packageInfo) {
        List<String> classes = new ArrayList<>();
        try {
            DexFile df = new DexFile(packageInfo.applicationInfo.sourceDir);
            for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
                classes.add(iter.nextElement());
            }
        } catch (IOException e) {
            Log.e(DexService.class.getSimpleName(), e.getLocalizedMessage());
        }
        return classes;
    }
}
