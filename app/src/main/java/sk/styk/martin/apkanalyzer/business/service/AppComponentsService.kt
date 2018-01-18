package sk.styk.martin.apkanalyzer.business.service

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import sk.styk.martin.apkanalyzer.model.detail.ActivityData
import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData
import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData
import sk.styk.martin.apkanalyzer.model.detail.ServiceData
import java.util.*

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class AppComponentsService {

    fun getActivities(packageInfo: PackageInfo, packageManager: PackageManager): List<ActivityData> {

        val activities = packageInfo.activities ?: return ArrayList(0)

        return activities.mapTo(ArrayList<ActivityData>(packageInfo.activities.size)) {
            ActivityData(it.name,
                    it.packageName,
                    it.loadLabel(packageManager)?.toString(),
                    it.targetActivity,
                    it.permission,
                    it.parentActivityName,
                    it.exported)
        }
    }

    fun getServices(packageInfo: PackageInfo): List<ServiceData> {

        val services = packageInfo.services ?: return ArrayList(0)

        return services.mapTo(ArrayList<ServiceData>(packageInfo.services.size)) {
            ServiceData(it.name,
                    it.permission,
                    it.exported,
                    it.flags and ServiceInfo.FLAG_STOP_WITH_TASK >0,
                    it.flags and ServiceInfo.FLAG_SINGLE_USER > 0,
                    it.flags and ServiceInfo.FLAG_ISOLATED_PROCESS > 0,
                    it.flags and ServiceInfo.FLAG_EXTERNAL_SERVICE > 0)
        }
    }

    fun getContentProviders(packageInfo: PackageInfo): List<ContentProviderData> {

        val providers = packageInfo.providers ?: return ArrayList(0)

        return providers.mapTo(ArrayList<ContentProviderData>(packageInfo.providers.size)) {
            ContentProviderData(it.name, it.authority, it.readPermission, it.writePermission, it.exported)
        }
    }

    fun getBroadcastReceivers(packageInfo: PackageInfo): List<BroadcastReceiverData> {

        val receivers = packageInfo.receivers ?: return ArrayList(0)

        return receivers.mapTo(ArrayList<BroadcastReceiverData>(packageInfo.receivers.size)) {
            BroadcastReceiverData(it.name, it.permission, it.exported)
        }
    }
}
