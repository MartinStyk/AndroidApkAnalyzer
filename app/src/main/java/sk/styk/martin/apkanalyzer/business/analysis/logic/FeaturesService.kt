package sk.styk.martin.apkanalyzer.business.analysis.logic

import android.content.pm.FeatureInfo
import android.content.pm.PackageInfo
import android.support.annotation.WorkerThread
import sk.styk.martin.apkanalyzer.model.detail.FeatureData
import java.util.*

/**
 * @author Martin Styk
 * @version 12.10.2017.
 */
@WorkerThread
class FeaturesService {

    fun get(packageInfo: PackageInfo): List<FeatureData> {

        val featureInfos = packageInfo.reqFeatures ?: return ArrayList(0)

        return featureInfos.mapTo(ArrayList(featureInfos.size)) {
            FeatureData(
                    name = it.name ?: it.glEsVersion,
                    isRequired = (it.flags and FeatureInfo.FLAG_REQUIRED) > 0
            )
        }
    }
}
