package sk.styk.martin.apkanalyzer.business.task;

import android.content.Context;

import java.util.List;

import sk.styk.martin.apkanalyzer.business.service.launcher.AppBasicDataService;
import sk.styk.martin.apkanalyzer.model.list.AppListData;

/**
 * Loader async task for item for AppListDialog
 * <p>
 * @author Martin Styk
 * @version 05.01.2018.
 */
public class AppListFromPackageNamesLoader extends ApkAnalyzerAbstractAsyncLoader<List<AppListData>> {

    public static final int ID = 5;
    private final AppBasicDataService appBasicDataService;

    private List<String> packageNames;

    public AppListFromPackageNamesLoader(Context context, List<String> packageNames) {
        super(context);
        this.packageNames = packageNames;
        appBasicDataService = new AppBasicDataService(context.getPackageManager());
    }

    @Override
    public List<AppListData> loadInBackground() {
        return appBasicDataService.getForPackageNames(packageNames);
    }

}

