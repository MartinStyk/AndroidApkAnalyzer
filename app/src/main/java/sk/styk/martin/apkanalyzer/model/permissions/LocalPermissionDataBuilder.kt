package sk.styk.martin.apkanalyzer.model.permissions

import sk.styk.martin.apkanalyzer.model.detail.PermissionData
import sk.styk.martin.apkanalyzer.model.detail.UsedPermissionData
import java.util.*

/**
 * @author Martin Styk
 * @version 13.01.2018.
 */
class LocalPermissionDataBuilder {

    private val data = HashMap<PermissionData, MutableList<PermissionStatus>>()

    fun addAll(packageName: String, usedPermissionData: List<UsedPermissionData>?) {
        if (usedPermissionData == null)
            return

        for (data in usedPermissionData) {
            add(packageName, data)
        }
    }

    fun add(packageName: String, usedPermissionData: UsedPermissionData) {
        var packageNamesForGivenPermissions: MutableList<PermissionStatus>? = data[usedPermissionData.permissionData]

        if (packageNamesForGivenPermissions == null) {
            packageNamesForGivenPermissions = ArrayList()
            data[usedPermissionData.permissionData] = packageNamesForGivenPermissions
        }
        packageNamesForGivenPermissions.add(PermissionStatus(packageName, usedPermissionData.isGranted))
    }

    fun build(): List<LocalPermissionData> {

        val sortedSet = TreeSet<Map.Entry<PermissionData, List<PermissionStatus>>>(
                Comparator<Map.Entry<PermissionData, List<PermissionStatus>>>
                { entry1, entry2 -> entry2.value.size - entry1.value.size })
        sortedSet.addAll(data.entries)

        return sortedSet.mapTo(ArrayList<LocalPermissionData>(sortedSet.size)) { LocalPermissionData(it.key, it.value) }
    }
}

