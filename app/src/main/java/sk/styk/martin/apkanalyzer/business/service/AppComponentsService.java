package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.model.ActivityData;

/**
 * Created by Martin Styk on 30.06.2017.
 */
public class AppComponentsService {

    private PackageManager packageManager;

    public AppComponentsService(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public List<ActivityData> getActivities(@NonNull PackageInfo packageInfo) {

        ActivityInfo[] activityInfos = packageInfo.activities;
        if (activityInfos == null || activityInfos.length == 0) {
            return new ArrayList<>(0);
        }
        List<ActivityData> myDataList = new ArrayList<>(activityInfos.length);

        for (ActivityInfo activityInfo : activityInfos) {
            ActivityData myData = new ActivityData();
            myData.setName(activityInfo.name);
            myData.setLabel(activityInfo.loadLabel(packageManager).toString());
            myData.setParentName(activityInfo.parentActivityName);
            myData.setPermission(activityInfo.permission);

            myDataList.add(myData);
        }

        return myDataList;
    }


}
