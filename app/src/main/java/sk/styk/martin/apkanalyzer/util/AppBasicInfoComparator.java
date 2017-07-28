package sk.styk.martin.apkanalyzer.util;

import java.text.Collator;
import java.util.Comparator;

import sk.styk.martin.apkanalyzer.model.list.AppListData;

/**
 * Created by Martin Styk on 17.06.2017.
 */

public class AppBasicInfoComparator implements Comparator<AppListData> {

    public static final AppBasicInfoComparator INSTANCE = new AppBasicInfoComparator();

    private final Collator sCollator = Collator.getInstance();

    @Override
    public int compare(AppListData object1, AppListData object2) {
        return sCollator.compare(object1.getApplicationName(), object2.getApplicationName());
    }
}
