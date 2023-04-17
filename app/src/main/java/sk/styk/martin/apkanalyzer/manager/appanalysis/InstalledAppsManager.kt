package sk.styk.martin.apkanalyzer.manager.appanalysis

import android.content.pm.PackageManager
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.apkanalyzer.util.AppBasicInfoComparator
import javax.inject.Inject

class InstalledAppsManager @Inject constructor(val packageManager: PackageManager) {

    fun getAll(): List<AppListData> {
        val applications = packageManager.getInstalledPackages(0)
        val packages = ArrayList<AppListData>(applications.size)

        applications.forEach {
            if (it.applicationInfo != null) {
                packages.add(AppListData(it, packageManager))
            }
        }

        return packages.sortedWith(AppBasicInfoComparator.INSTANCE)
    }

    fun getForPackageNames(packageNames: List<String>): List<AppListData> {
        val packages = ArrayList<AppListData>(packageNames.size)

        packageNames.forEach {
            try {
                val packageInfo = packageManager.getPackageInfo(it, 0)
                if (packageInfo?.applicationInfo != null) {
                    packages.add(AppListData(packageInfo, packageManager))
                }
            } catch (e: PackageManager.NameNotFoundException) {
                // continue
            }
        }

        return packages.sortedWith(AppBasicInfoComparator.INSTANCE)
    }

    fun preload(appListData: List<AppListData>) {
        appListData.forEach {
            it.icon
            it.packageName
        }
    }
}
