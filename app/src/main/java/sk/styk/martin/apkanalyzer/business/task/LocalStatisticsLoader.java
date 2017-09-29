package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

import sk.styk.martin.apkanalyzer.business.service.AppBasicDataService;
import sk.styk.martin.apkanalyzer.business.service.LocalApplicationStatisticDataService;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataBuilder;

public class LocalStatisticsLoader extends ApkAnalyzerAbstractAsyncLoader<LocalStatisticsData> {
    public static final int ID = 3;

    private WeakReference<ProgressCallback> callbackReference;
    private AppBasicDataService appBasicDataService;
    private LocalApplicationStatisticDataService localAppDataService;

    public interface ProgressCallback {
        void onProgressChanged(int currentProgress, int maxProgress);
    }

    public LocalStatisticsLoader(Context context, ProgressCallback callback) {
        super(context);
        callbackReference = new WeakReference<>(callback);
        appBasicDataService = new AppBasicDataService(context.getPackageManager());
        localAppDataService = new LocalApplicationStatisticDataService(context.getPackageManager());
    }

    public void setCallbackReference(ProgressCallback progressCallback){
        callbackReference = new WeakReference<>(progressCallback);
    }

    @Override
    public LocalStatisticsData loadInBackground() {
        List<String> packageNames = appBasicDataService.getAllPackageNames();
        LocalStatisticsDataBuilder builder = new LocalStatisticsDataBuilder(packageNames.size());

        for (int i = 0; i < packageNames.size(); i++) {
            builder.add(localAppDataService.get(packageNames.get(i)));
            if (callbackReference.get() != null) {
                callbackReference.get().onProgressChanged(i, packageNames.size());
            }
        }

        return builder.build();
    }

}

