package sk.styk.martin.apkanalyzer.model.detail

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Class holding basic application metadata used in application detail view
 *
 *
 * @author Martin Styk
 * @version 22.06.2017.
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class AppDetailData(val analysisMode: AnalysisMode,
                         val generalData: GeneralData,
                         val certificateData: CertificateData,
                         val activityData: List<ActivityData>,
                         val serviceData: List<ServiceData>,
                         var contentProviderData: List<ContentProviderData>,
                         var broadcastReceiverData: List<BroadcastReceiverData>,
                         var permissionData: PermissionDataAggregate,
                         var featureData: List<FeatureData>,
                         var fileData: FileData,
                         var resourceData: ResourceData,
                         var classPathData: ClassPathData,
                         val packageInfo: PackageInfo) : Parcelable {

    val isAnalyzedApkFile: Boolean
        get() = AnalysisMode.APK_FILE == analysisMode

    val isAnalyzedInstalledPackage: Boolean
        get() = AnalysisMode.INSTALLED_PACKAGE == analysisMode

    enum class AnalysisMode {
        INSTALLED_PACKAGE,
        APK_FILE
    }
}
