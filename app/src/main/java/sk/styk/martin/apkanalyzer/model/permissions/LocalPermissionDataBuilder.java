package sk.styk.martin.apkanalyzer.model.permissions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import sk.styk.martin.apkanalyzer.model.detail.PermissionData;
import sk.styk.martin.apkanalyzer.model.detail.UsedPermissionData;

/**
 * @author Martin Styk
 * @version 13.01.2018.
 */
public class LocalPermissionDataBuilder {

    private Map<PermissionData, List<PermissionStatus>> data = new HashMap<>();

    public void addAll(String packageName, List<UsedPermissionData> usedPermissionData) {
        if (usedPermissionData == null)
            return;

        for (UsedPermissionData data : usedPermissionData) {
            add(packageName, data);
        }
    }

    public void add(String packageName, UsedPermissionData usedPermissionData) {
        if (usedPermissionData == null)
            return;

        List<PermissionStatus> packageNamesForGivenPermissions = data.get(usedPermissionData.getPermissionData());

        if (packageNamesForGivenPermissions == null) {
            packageNamesForGivenPermissions = new ArrayList<>();
            data.put(usedPermissionData.getPermissionData(), packageNamesForGivenPermissions);
        }
        packageNamesForGivenPermissions.add(new PermissionStatus(packageName, usedPermissionData.isGranted()));
    }

    public List<LocalPermissionData> build() {

        SortedSet<Map.Entry<PermissionData, List<PermissionStatus>>> sortedSet = new TreeSet<>(new Comparator<Map.Entry<PermissionData, List<PermissionStatus>>>() {
            @Override
            public int compare(Map.Entry<PermissionData, List<PermissionStatus>> entry1, Map.Entry<PermissionData, List<PermissionStatus>> entry2) {
                return entry2.getValue().size() - entry1.getValue().size();
            }
        });
        sortedSet.addAll(data.entrySet());

        return convertToList(sortedSet);

    }

    private List<LocalPermissionData> convertToList(SortedSet<Map.Entry<PermissionData, List<PermissionStatus>>> sortedEntries) {

        List<LocalPermissionData> result = new ArrayList<>(sortedEntries.size());

        for (Map.Entry<PermissionData, List<PermissionStatus>> entry : sortedEntries) {
            result.add(new LocalPermissionData(entry.getKey(), entry.getValue()));
        }

        return result;
    }
}

