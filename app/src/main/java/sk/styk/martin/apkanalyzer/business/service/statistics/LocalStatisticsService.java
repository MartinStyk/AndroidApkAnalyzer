package sk.styk.martin.apkanalyzer.business.service.statistics;

import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import sk.styk.martin.apkanalyzer.business.service.AppBasicDataService;
import sk.styk.martin.apkanalyzer.business.service.AppComponentsService;
import sk.styk.martin.apkanalyzer.business.service.CertificateService;
import sk.styk.martin.apkanalyzer.business.service.FileDataService;
import sk.styk.martin.apkanalyzer.business.service.GeneralDataService;
import sk.styk.martin.apkanalyzer.business.service.PermissionsService;
import sk.styk.martin.apkanalyzer.business.service.ResourceService;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsAppData;
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData;

/**
 * Created by Martin Styk on 28.07.2017.
 */
public class LocalStatisticsService {

    private PackageManager packageManager;

    private AppBasicDataService appBasicDataService;
    private LocalApplicationStatisticDataService localAppDataService;


    public LocalStatisticsService(PackageManager packageManager) {
        this.packageManager = packageManager;
        appBasicDataService = new AppBasicDataService(packageManager);
        localAppDataService = new LocalApplicationStatisticDataService(packageManager);
    }

    public LocalStatisticsData getLocalStatistics() {

        List<String> packageNames = appBasicDataService.getAllPackageNames();
        List<LocalStatisticsAppData> appData = new ArrayList<>(packageNames.size());

        for (String packageName : packageNames) {
            appData.add(localAppDataService.get(packageName));
        }

        // TODO get aggregate data;
        return null;
    }

}
