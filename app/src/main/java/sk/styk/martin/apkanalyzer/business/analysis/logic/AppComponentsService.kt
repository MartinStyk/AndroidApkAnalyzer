package sk.styk.martin.apkanalyzer.business.analysis.logic

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import androidx.annotation.WorkerThread
import sk.styk.martin.apkanalyzer.model.detail.ActivityData
import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData
import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData
import sk.styk.martin.apkanalyzer.model.detail.ServiceData
import java.util.*

@WorkerThread
class AppComponentsService {

    fun getActivities(packageInfo: PackageInfo, packageManager: PackageManager): List<ActivityData> {

        val activities = packageInfo.activities ?: return ArrayList(0)

        return activities.mapTo(ArrayList(packageInfo.activities.size)) {
            ActivityData(it.name,
                    it.packageName,
                    it.loadLabel(packageManager).toString(),
                    it.targetActivity,
                    it.permission,
                    it.parentActivityName,
                    it.exported)
        }
    }

    fun getServices(packageInfo: PackageInfo): List<ServiceData> {

        val services = packageInfo.services ?: return ArrayList(0)

        return services.mapTo(ArrayList<ServiceData>(packageInfo.services.size)) {
            ServiceData(name = it.name,
                    permission = it.permission,
                    isExported = it.exported,
                    isStopWithTask = it.flags and ServiceInfo.FLAG_STOP_WITH_TASK > 0,
                    isSingleUser = it.flags and ServiceInfo.FLAG_SINGLE_USER > 0,
                    isIsolatedProcess = it.flags and ServiceInfo.FLAG_ISOLATED_PROCESS > 0,
                    isExternalService = it.flags and ServiceInfo.FLAG_EXTERNAL_SERVICE > 0)
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
