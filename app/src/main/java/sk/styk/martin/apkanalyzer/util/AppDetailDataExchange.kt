package sk.styk.martin.apkanalyzer.util

import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import java.lang.IllegalArgumentException

private const val CACHE_CAPACITY = 5
private const val CACHE_LOAD_FACTOR = 0.75f

object AppDetailDataExchange {

    private val appDataCache = object : LinkedHashMap<String, AppDetailData>(CACHE_CAPACITY, CACHE_LOAD_FACTOR, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, AppDetailData>) = size > CACHE_CAPACITY
    }

    @Synchronized
    fun save(appDetailData: AppDetailData) = appDetailData.generalData.packageName.let {
        appDataCache[it] = appDetailData
        it
    }

    @Synchronized
    fun get(packageName: String) = appDataCache[packageName]
}