package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;
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

    public List<String> get(@NonNull PackageInfo packageInfo) {
        List<String> classes = new ArrayList<>();
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;

        if (applicationInfo != null) {
            try {
                DexFile dexFile = new DexFile(applicationInfo.sourceDir);
                for (Enumeration<String> iterator = dexFile.entries(); iterator.hasMoreElements(); ) {
                    classes.add(iterator.nextElement());
                }
            } catch (IOException e) {
                Log.e(DexService.class.getSimpleName(), e.getLocalizedMessage());
            }
        }
        
        return classes;
    }
}
