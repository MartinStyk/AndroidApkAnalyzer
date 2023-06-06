package sk.styk.martin.apkanalyzer.core.appanalysis

import android.content.pm.FeatureInfo
import android.content.pm.PackageInfo
import sk.styk.martin.apkanalyzer.core.appanalysis.model.FeatureData
import javax.inject.Inject

class FeaturesManager @Inject constructor() {

    fun get(packageInfo: PackageInfo): List<FeatureData> {
        val featureInfos = packageInfo.reqFeatures ?: return ArrayList(0)

        return featureInfos.mapTo(ArrayList(featureInfos.size)) {
            FeatureData(
                name = it.name ?: it.glEsVersion,
                isRequired = (it.flags and FeatureInfo.FLAG_REQUIRED) > 0,
            )
        }
    }
}
