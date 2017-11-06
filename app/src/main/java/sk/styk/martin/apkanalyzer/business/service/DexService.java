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
import sk.styk.martin.apkanalyzer.model.detail.ClassPathData;

/**
 * Created by Martin Styk on 21.10.2017.
 */
public class DexService {

    public ClassPathData get(@NonNull PackageInfo packageInfo) {

        List<String> packageClasses = new ArrayList<>();
        List<String> otherClasses = new ArrayList<>();
        ClassPathData classPathData = new ClassPathData();

        ApplicationInfo applicationInfo = packageInfo.applicationInfo;

        if (applicationInfo != null) {
            String packageName = applicationInfo.packageName;
            try {
                DexFile dexFile = new DexFile(applicationInfo.sourceDir);
                for (Enumeration<String> iterator = dexFile.entries(); iterator.hasMoreElements(); ) {
                    String className = iterator.nextElement();
                    if (className != null && className.startsWith(packageName))
                        packageClasses.add(className);
                    else
                        otherClasses.add(className);
                }
            } catch (IOException e) {
                Log.e(DexService.class.getSimpleName(), e.getLocalizedMessage());
            }
        }

        classPathData.setPackageClasses(packageClasses);
        classPathData.setOtherClasses(otherClasses);

        return classPathData;
    }
}
