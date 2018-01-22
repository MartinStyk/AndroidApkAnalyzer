package sk.styk.martin.apkanalyzer.activity.detailfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateUtils
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_app_detail_general.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.model.detail.GeneralData
import sk.styk.martin.apkanalyzer.util.InstallLocationHelper

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */

class AppDetailFragment_General : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_detail_general, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data: GeneralData = arguments.getParcelable(AppDetailFragment.ARG_CHILD)
                ?: throw IllegalArgumentException("data null")

        item_application_name.valueText = data.applicationName
        item_package_name.valueText = data.packageName
        item_process_name.valueText = data.processName
        item_version_name.valueText = data.versionName
        item_version_code.valueText = data.versionCode.toString()
        item_system_application.valueText = if (data.isSystemApp) getString(R.string.yes) else getString(R.string.no)
        item_uid.valueText = data.uid?.toString()
        item_application_description.valueText = data.description
        item_application_app_installer.valueText = data.appInstaller
        item_application_app_source.valueText = data.source.toString()
        item_target_sdk.valueText = data.targetSdkVersion.toString()
        item_target_android_version.valueText = data.targetSdkLabel
        item_min_sdk.valueText = data.minSdkVersion.toString()
        item_min_android_version.valueText = data.minSdkLabel
        item_apk_directory.valueText = data.apkDirectory
        item_data_directory.valueText = data.dataDirectory
        item_install_location.valueText = InstallLocationHelper.showLocalizedLocation(data.installLocation, context)
        item_apk_size.valueText = Formatter.formatShortFileSize(context, data.apkSize)

        item_first_install_time.valueText = if (data.firstInstallTime != null)
            DateUtils.formatDateTime(context, data.firstInstallTime, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME) else null
        item_last_update_time.valueText = if (data.lastUpdateTime != null)
            DateUtils.formatDateTime(activity, data.lastUpdateTime, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME) else null

    }
}
