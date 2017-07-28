package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;

import sk.styk.martin.apkanalyzer.business.service.AppDetailDataService;
import sk.styk.martin.apkanalyzer.business.service.statistics.LocalStatisticsService;
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsAppData;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData;

public class LocalStatisticsLoader extends ApkAnalyzerAbstractAsyncLoader<LocalStatisticsData> {
    public static final int ID = 3;

    private LocalStatisticsService localStatisticsService;

    public LocalStatisticsLoader(Context context) {
        super(context);
        localStatisticsService = new LocalStatisticsService(context.getPackageManager());
    }

    @Override
    public LocalStatisticsData loadInBackground() {
        return localStatisticsService.getLocalStatistics();
    }

}

