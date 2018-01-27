package sk.styk.martin.apkanalyzer.business.analysis.logic

import android.content.pm.FeatureInfo
import android.content.pm.PackageInfo
import sk.styk.martin.apkanalyzer.model.detail.FeatureData
import java.util.*

/**
 * @author Martin Styk
 * @version 12.10.2017.
 */
class FeaturesService {

    fun get(packageInfo: PackageInfo): List<FeatureData> {

        val featureInfos = packageInfo.reqFeatures ?: return ArrayList(0)

        return featureInfos.mapTo(ArrayList<FeatureData>(featureInfos.size)) {
            FeatureData(
                    name = it.name ?: it.glEsVersion,
                    isRequired = (it.flags and FeatureInfo.FLAG_REQUIRED) > 0
            )
        }
    }
}
