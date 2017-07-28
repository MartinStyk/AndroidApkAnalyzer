package sk.styk.martin.apkanalyzer.business.service.statistics;

import android.content.pm.PackageManager;

import java.util.List;

import sk.styk.martin.apkanalyzer.business.service.AppBasicDataService;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataBuilder;

/**
 * Created by Martin Styk on 28.07.2017.
 */
public class LocalStatisticsService {

    private AppBasicDataService appBasicDataService;
    private LocalApplicationStatisticDataService localAppDataService;


    public LocalStatisticsService(PackageManager packageManager) {
        appBasicDataService = new AppBasicDataService(packageManager);
        localAppDataService = new LocalApplicationStatisticDataService(packageManager);
    }

    public LocalStatisticsData getLocalStatistics() {

        List<String> packageNames = appBasicDataService.getAllPackageNames();
        LocalStatisticsDataBuilder builder = new LocalStatisticsDataBuilder(packageNames.size());

        for (String packageName : packageNames) {
            builder.add(localAppDataService.get(packageName));
        }

        return builder.build();
    }

}
