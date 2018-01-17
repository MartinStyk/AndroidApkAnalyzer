package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

import sk.styk.martin.apkanalyzer.business.service.AppBasicDataService;
import sk.styk.martin.apkanalyzer.business.service.LocalPermissionsDataService;
import sk.styk.martin.apkanalyzer.model.list.AppListData;
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData;
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionDataBuilder;

public class LocalPermissionsLoader extends ApkAnalyzerAbstractAsyncLoader<List<LocalPermissionData>> {
    public static final int ID = 6;

    private WeakReference<ProgressCallback> callbackReference;
    private AppBasicDataService appBasicDataService;
    private LocalPermissionsDataService localPermissionsDataService;

    public interface ProgressCallback {
        void onProgressChanged(int currentProgress, int maxProgress);
    }

    public LocalPermissionsLoader(Context context, ProgressCallback callback) {
        super(context);
        callbackReference = new WeakReference<>(callback);
        appBasicDataService = new AppBasicDataService(context.getPackageManager());
        localPermissionsDataService = new LocalPermissionsDataService(context.getPackageManager());
    }

    public void setCallbackReference(ProgressCallback progressCallback){
        callbackReference = new WeakReference<>(progressCallback);
    }

    @Override
    public List<LocalPermissionData> loadInBackground() {
        List<AppListData> allApps = appBasicDataService.getAll();
        LocalPermissionDataBuilder builder = new LocalPermissionDataBuilder();

        for (int i = 0; i < allApps.size(); i++) {
            String packageName = allApps.get(i).getPackageName();

            builder.addAll(packageName, localPermissionsDataService.get(packageName));

            // TODO insert into DB and check new perms
            if (callbackReference.get() != null) {
                callbackReference.get().onProgressChanged(i, allApps.size());
            }
        }

        return builder.build();
    }

}

