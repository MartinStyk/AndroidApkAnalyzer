package sk.styk.martin.apkanalyzer.ui.appdetail.page.general

import android.text.format.DateUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.core.appanalysis.model.InstallLocation
import sk.styk.martin.apkanalyzer.manager.clipboard.ClipBoardManager
import sk.styk.martin.apkanalyzer.core.appanalysis.model.AppDetailData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.appdetail.adapters.DetailInfoAdapter
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragmentViewModel
import sk.styk.martin.apkanalyzer.util.Formatter
import sk.styk.martin.apkanalyzer.util.TextInfo

class AppGeneralDetailsFragmentViewModel @AssistedInject constructor(
    @Assisted appDetailFragmentViewModel: AppDetailFragmentViewModel,
    private val detailInfoAdapter: DetailInfoAdapter,
    clipBoardManager: ClipBoardManager,
    private val formatter: Formatter,
) : AppDetailPageFragmentViewModel(appDetailFragmentViewModel, detailInfoAdapter, clipBoardManager) {

    override fun onDataReceived(appDetailData: AppDetailData): Boolean {
        val generalData = appDetailData.generalData
        detailInfoAdapter.info = listOfNotNull(
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.application_name),
                TextInfo.from(generalData.applicationName),
                TextInfo.from(R.string.application_name_description),
            ),
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.package_name),
                TextInfo.from(generalData.packageName),
                TextInfo.from(R.string.package_name_description),
            ),
            generalData.processName?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.process_name),
                    TextInfo.from(it),
                    TextInfo.from(R.string.process_name_description),
                )
            },
            generalData.versionName?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.version_name),
                    TextInfo.from(it),
                    TextInfo.from(R.string.version_name_description),
                )
            },
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.version_code),
                TextInfo.from(generalData.versionCode.toString()),
                TextInfo.from(R.string.version_code_description),
            ),
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.system_application),
                TextInfo.from(if (generalData.isSystemApp) R.string.yes else R.string.no),
                TextInfo.from(R.string.system_application_description),
            ),
            generalData.uid?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.uid),
                    TextInfo.from(generalData.uid.toString()),
                    TextInfo.from(R.string.uid_description),
                )
            },
            generalData.description?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.description),
                    TextInfo.from(it),
                    TextInfo.from(R.string.description_description),
                )
            },
            generalData.appInstaller?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.app_installer),
                    TextInfo.from(it),
                    TextInfo.from(R.string.description_app_installer),
                )
            },
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.app_source),
                TextInfo.from(generalData.source.toString()),
                TextInfo.from(R.string.description_app_source),
            ),
            generalData.targetSdkVersion?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.target_sdk),
                    TextInfo.from(generalData.targetSdkVersion.toString()),
                    TextInfo.from(R.string.target_sdk_description),
                )
            },
            generalData.targetSdkLabel?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.target_version),
                    TextInfo.from(it),
                    TextInfo.from(R.string.target_version_description),
                )
            },
            generalData.minSdkVersion?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.min_sdk),
                    TextInfo.from(generalData.minSdkVersion.toString()),
                    TextInfo.from(R.string.min_sdk_description),
                )
            },
            generalData.minSdkLabel?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.min_version),
                    TextInfo.from(it),
                    TextInfo.from(R.string.min_version_description),
                )
            },
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.apk_directory),
                TextInfo.from(generalData.apkDirectory),
                TextInfo.from(R.string.apk_directory_description),
            ),
            generalData.dataDirectory?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.data_directory),
                    TextInfo.from(it),
                    TextInfo.from(R.string.data_directory_description),
                )
            },
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.install_loc),
                TextInfo.from(
                    when (generalData.installLocation) {
                        InstallLocation.AUTO -> R.string.install_loc_auto
                        InstallLocation.INTERNAL -> R.string.install_loc_internal_only
                        InstallLocation.EXTERNAL -> R.string.install_loc_prefer_external
                        else -> R.string.install_loc_internal_only
                    },
                ),
                TextInfo.from(R.string.install_loc_description),
            ),
            DetailInfoAdapter.DetailInfo(
                TextInfo.from(R.string.apk_size),
                TextInfo.from(formatter.formatShortFileSize(generalData.apkSize)),
                TextInfo.from(R.string.apk_size_description),
            ),
            generalData.firstInstallTime?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.first_install),
                    TextInfo.from(formatter.formatDateTime(it, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME)),
                    TextInfo.from(R.string.first_install_description),
                )
            },
            generalData.lastUpdateTime?.let {
                DetailInfoAdapter.DetailInfo(
                    TextInfo.from(R.string.last_update),
                    TextInfo.from(formatter.formatDateTime(it, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME)),
                    TextInfo.from(R.string.last_update_description),
                )
            },
        )
        return true
    }

    @AssistedFactory
    interface Factory {
        fun create(appDetailFragmentViewModel: AppDetailFragmentViewModel): AppGeneralDetailsFragmentViewModel
    }
}
