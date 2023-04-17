package sk.styk.martin.apkanalyzer.model.statistics

import sk.styk.martin.apkanalyzer.manager.appanalysis.MAX_SDK_VERSION
import sk.styk.martin.apkanalyzer.model.InstallLocation
import sk.styk.martin.apkanalyzer.model.detail.AppSource

class StatisticsDataBuilder(datasetSize: Int) {
    private val arraySize = datasetSize + 1

    private var analyzeSuccess = 0
    private var analyzeFailed = 0

    private var systemApps: Int = 0
    private val installLocation = HashMap<InstallLocation, MutableList<String>>(3)
    private val targetSdk = HashMap<Int, MutableList<String>>(MAX_SDK_VERSION)
    private val minSdk = HashMap<Int, MutableList<String>>(MAX_SDK_VERSION)
    private val appSource = HashMap<AppSource, MutableList<String>>(AppSource.values().size)

    private val apkSize = FloatArray(arraySize)

    private val signAlgorithm = HashMap<String, MutableList<String>>(5)

    private val activities = FloatArray(arraySize)
    private val services = FloatArray(arraySize)
    private val providers = FloatArray(arraySize)
    private val receivers = FloatArray(arraySize)

    private val usedPermissions = FloatArray(arraySize)
    private val definedPermissions = FloatArray(arraySize)

    fun build(): StatisticsData {
        return StatisticsData(
            analyzeSuccess = PercentagePair(analyzeSuccess, analyzeSuccess + analyzeFailed),
            analyzeFailed = PercentagePair(analyzeFailed, analyzeSuccess + analyzeFailed),
            systemApps = PercentagePair(systemApps, analyzeSuccess),
            installLocation = installLocation,
            targetSdk = targetSdk,
            minSdk = minSdk,
            appSource = appSource,
            apkSize = MathStatisticsBuilder(apkSize).build(),
            signAlgorithm = signAlgorithm,

            activities = MathStatisticsBuilder(activities).build(),
            services = MathStatisticsBuilder(services).build(),
            receivers = MathStatisticsBuilder(receivers).build(),
            providers = MathStatisticsBuilder(providers).build(),

            usedPermissions = MathStatisticsBuilder(usedPermissions).build(),
            definedPermissions = MathStatisticsBuilder(definedPermissions).build(),
        )
    }

    fun add(appData: StatisticsAppData?) {
        if (appData == null) {
            analyzeFailed++
            return
        }

        analyzeSuccess++
        if (appData.isSystemApp) systemApps++
        addToMap(installLocation, InstallLocation.from(appData.installLocation), appData.packageName)
        addToMap(targetSdk, appData.targetSdk, appData.packageName)
        addToMap(minSdk, appData.minSdk, appData.packageName)
        apkSize[analyzeSuccess] = appData.apkSize.toFloat()
        addToMap(signAlgorithm, appData.signAlgorithm, appData.packageName)
        addToMap(appSource, appData.appSource, appData.packageName)

        activities[analyzeSuccess] = appData.activities.toFloat()
        services[analyzeSuccess] = appData.services.toFloat()
        providers[analyzeSuccess] = appData.providers.toFloat()
        receivers[analyzeSuccess] = appData.receivers.toFloat()

        usedPermissions[analyzeSuccess] = appData.usedPermissions.toFloat()
        definedPermissions[analyzeSuccess] = appData.definedPermissions.toFloat()
    }

    private fun <T> addToMap(map: MutableMap<T, MutableList<String>>, key: T, packageName: String) {
        var apps: MutableList<String>? = map[key]

        if (apps != null) {
            apps.add(packageName)
        } else {
            apps = mutableListOf(packageName)
        }

        map[key] = apps
    }
}
