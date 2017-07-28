package sk.styk.martin.apkanalyzer.business.service;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.model.detail.ActivityData;
import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData;
import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData;
import sk.styk.martin.apkanalyzer.model.detail.ServiceData;

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

    public List<ServiceData> getServices(@NonNull PackageInfo packageInfo) {

        ServiceInfo[] serviceInfos = packageInfo.services;
        if (serviceInfos == null || serviceInfos.length == 0) {
            return new ArrayList<>(0);
        }
        List<ServiceData> myDataList = new ArrayList<>(serviceInfos.length);

        for (ServiceInfo serviceInfo : serviceInfos) {
            ServiceData myData = new ServiceData();
            myData.setName(serviceInfo.name);
            myData.setPermission(serviceInfo.permission);
            myData.setExported(serviceInfo.exported);
            myData.setExternalService((serviceInfo.flags & ServiceInfo.FLAG_EXTERNAL_SERVICE) > 0);
            myData.setIsolatedProcess((serviceInfo.flags & ServiceInfo.FLAG_ISOLATED_PROCESS) > 0);
            myData.setSingleUser((serviceInfo.flags & ServiceInfo.FLAG_SINGLE_USER) > 0);
            myData.setStopWithTask((serviceInfo.flags & ServiceInfo.FLAG_STOP_WITH_TASK) > 0);

            myDataList.add(myData);
        }

        return myDataList;
    }

    public List<ContentProviderData> getContentProviders(@NonNull PackageInfo packageInfo) {

        ProviderInfo[] providerInfos = packageInfo.providers;
        if (providerInfos == null || providerInfos.length == 0) {
            return new ArrayList<>(0);
        }
        List<ContentProviderData> myDataList = new ArrayList<>(providerInfos.length);

        for (ProviderInfo providerInfo : providerInfos) {
            ContentProviderData myData = new ContentProviderData();
            myData.setName(providerInfo.name);
            myData.setExported(providerInfo.exported);
            myData.setAuthority(providerInfo.authority);
            myData.setReadPermission(providerInfo.readPermission);
            myData.setWritePermission(providerInfo.writePermission);

            myDataList.add(myData);
        }

        return myDataList;
    }

    public List<BroadcastReceiverData> getBroadcastReceivers(@NonNull PackageInfo packageInfo) {

        ActivityInfo[] receiverInfos = packageInfo.receivers;
        if (receiverInfos == null || receiverInfos.length == 0) {
            return new ArrayList<>(0);
        }
        List<BroadcastReceiverData> myDataList = new ArrayList<>(receiverInfos.length);

        for (ActivityInfo receiverInfo : receiverInfos) {
            BroadcastReceiverData myData = new BroadcastReceiverData();
            myData.setName(receiverInfo.name);
            myData.setExported(receiverInfo.exported);

            myDataList.add(myData);
        }

        return myDataList;
    }


}
