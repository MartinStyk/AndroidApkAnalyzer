package sk.styk.martin.apkanalyzer.business.analysis.logic.launcher

import android.content.pm.PackageManager
import sk.styk.martin.apkanalyzer.model.detail.AppSource
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.apkanalyzer.util.AppBasicInfoComparator
import java.util.*
import javax.inject.Inject

class InstalledAppsManager @Inject constructor(val packageManager: PackageManager) {

    fun getAllPackageNames(): List<String> {
        val applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        return applications.mapTo(ArrayList<String>(applications.size)) { it.packageName }.sorted()
    }

    fun getAll(): List<AppListData> {
        val applications = packageManager.getInstalledPackages(0)
        val packages = ArrayList<AppListData>(applications.size)

        applications.forEach {
            if (it.applicationInfo != null)
                packages.add(AppListData(it, packageManager))
        }

        return packages.sortedWith(AppBasicInfoComparator.INSTANCE)
    }

    fun getForSources(allowSystem: Boolean, vararg appSources: AppSource): List<AppListData> {
        val appSourceList = Arrays.asList(*appSources)
        return getAll().filter { appSourceList.contains(it.source) && (allowSystem || !it.isSystemApp) }
    }

    fun getForPackageNames(packageNames: List<String>): List<AppListData> {

        val packages = ArrayList<AppListData>(packageNames.size)

        packageNames.forEach {
            try {
                val packageInfo = packageManager.getPackageInfo(it, 0)
                if (packageInfo?.applicationInfo != null)
                    packages.add(AppListData(packageInfo, packageManager))

            } catch (e: PackageManager.NameNotFoundException) {
                // continue
            }
        }

        return packages.sortedWith(AppBasicInfoComparator.INSTANCE)
    }

}
