package sk.styk.martin.apkanalyzer.util

import sk.styk.martin.apkanalyzer.model.list.AppListData
import java.text.Collator
import java.util.*

class AppBasicInfoComparator : Comparator<AppListData> {

    private val sCollator = Collator.getInstance()

    override fun compare(object1: AppListData, object2: AppListData): Int {
        return sCollator.compare(object1.applicationName, object2.applicationName)
    }

    companion object {
        val INSTANCE = AppBasicInfoComparator()
    }
}
