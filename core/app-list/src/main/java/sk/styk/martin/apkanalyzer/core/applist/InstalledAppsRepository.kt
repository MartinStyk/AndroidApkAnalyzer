package sk.styk.martin.apkanalyzer.core.applist

import android.content.pm.PackageManager
import sk.styk.martin.apkanalyzer.core.applist.model.AppListData
import java.text.Collator
import javax.inject.Inject

class InstalledAppsRepository @Inject constructor(val packageManager: PackageManager) {

    fun getAll(): List<AppListData> {
        val applications = packageManager.getInstalledPackages(0)
        val packages = ArrayList<AppListData>(applications.size)

        applications.forEach {
            if (it.applicationInfo != null) {
                packages.add(AppListData(it, packageManager))
            }
        }

        return packages.sortedWith(comparator)
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

        return packages.sortedWith(comparator)
    }

    fun preload(appListData: List<AppListData>) {
        appListData.forEach {
            it.icon
            it.packageName
        }
    }

    private val comparator by lazy {
        object : Comparator<AppListData> {
            private val sCollator = Collator.getInstance()
            override fun compare(object1: AppListData, object2: AppListData): Int {
                return sCollator.compare(object1.applicationName, object2.applicationName)
            }
        }
    }

}
